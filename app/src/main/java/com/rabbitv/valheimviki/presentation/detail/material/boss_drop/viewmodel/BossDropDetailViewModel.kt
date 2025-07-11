package com.rabbitv.valheimviki.presentation.detail.material.boss_drop.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.boss_drop.model.BossDropUiState
import com.rabbitv.valheimviki.utils.Constants.BOSS_DROP_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BossDropDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _materialId: String = checkNotNull(savedStateHandle[BOSS_DROP_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _boss = MutableStateFlow<MainBoss?>(null)
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_material,
		_boss,
		favoriteUseCases.isFavorite(_materialId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { material, boss, isFavorite, isLoading, error ->
		BossDropUiState(
			material = material,
			boss = boss,
			isFavorite = isFavorite,
			isLoading = isLoading,
			error = error
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		BossDropUiState()
	)

	init {
		loadBossDropData()
	}

	internal fun loadBossDropData() {

		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true
				_error.value = null


				val materialDeferred = async {
					materialUseCases.getMaterialById(_materialId).first()
				}


				val relationObjectsDeferred = async {
					relationUseCases.getRelatedIdsUseCase(_materialId).first()
				}


				val material = materialDeferred.await()
				val relationObjects = relationObjectsDeferred.await()

				_material.value = material

				val relationIds = relationObjects.map { it.id }


				val creaturesDeferred = async {
					creatureUseCases.getCreatureByRelationAndSubCategory(
						relationIds,
						CreatureSubCategory.BOSS
					).first()?.toMainBoss()
				}

				_boss.value = creaturesDeferred.await()

			} catch (e: Exception) {
				Log.e("BossDropDetailViewModel", "General fetch error: ${e.message}", e)
				_error.value = e.message
			} finally {
				_isLoading.value = false
			}

		}
	}

	fun toggleFavorite(favorite: Favorite, currentIsFavorite: Boolean) {
		viewModelScope.launch {
			if (currentIsFavorite) {
				favoriteUseCases.deleteFavoriteUseCase(favorite)
			} else {
				favoriteUseCases.addFavoriteUseCase(favorite)
			}
		}
	}
}