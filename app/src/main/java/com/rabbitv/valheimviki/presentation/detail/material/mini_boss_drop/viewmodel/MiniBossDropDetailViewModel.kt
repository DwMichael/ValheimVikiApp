package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBoss
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model.MiniBossDropUiState
import com.rabbitv.valheimviki.utils.Constants.MINI_BOSS_DROP_KEY
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
class MiniBossDropDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _materialId: String = checkNotNull(savedStateHandle[MINI_BOSS_DROP_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _boss = MutableStateFlow<MiniBoss?>(null)
	private val _npc = MutableStateFlow<NPC?>(null)
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState = combine(
		_material,
		_boss,
		_npc,
		favoriteUseCases.isFavorite(_materialId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		MiniBossDropUiState(
			material = values[0] as Material?,
			miniBoss = values[1] as MiniBoss?,
			npc = values[2] as NPC?,
			isFavorite = values[3] as Boolean,
			isLoading = values[4] as Boolean,
			error = values[5] as String?,
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		MiniBossDropUiState()
	)

	init {
		loadMiniBossDropData()
	}

	internal fun loadMiniBossDropData() {

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


				val miniBossDeferred = async {
					creatureUseCases.getCreatureByRelationAndSubCategory(
						relationIds,
						CreatureSubCategory.MINI_BOSS
					).first()?.toMiniBoss()
				}

				val npcDeferred = async {
					creatureUseCases.getCreatureByRelationAndSubCategory(
						relationIds,
						CreatureSubCategory.NPC
					).first()?.toNPC()
				}

				_boss.value = miniBossDeferred.await()
				_npc.value = npcDeferred.await()

			} catch (e: Exception) {
				Log.e("MiniBossDropDetailViewModel", "General fetch error: ${e.message}", e)
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