@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.detail.biome.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.presentation.detail.biome.model.BiomeDetailUiState
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BiomeDetailScreenViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val biomeUseCases: BiomeUseCases,
	private val creaturesUseCase: CreatureUseCases,
	private val relationsUseCase: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val treeUseCases: TreeUseCases,
	private val oreDepositUseCases: OreDepositUseCases,
	private val favoriteUseCases: FavoriteUseCases,
) : ViewModel() {
	private val _biomeId: String = checkNotNull(savedStateHandle[BIOME_ARGUMENT_KEY])
	private val _biome = MutableStateFlow<Biome?>(null)
	private val _mainBoss = MutableStateFlow<MainBoss?>(null)
	private val _relatedCreatures = MutableStateFlow<List<Creature>>(emptyList())
	private val _relatedOreDeposits = MutableStateFlow<List<OreDeposit>>(emptyList())
	private val _relatedMaterials = MutableStateFlow<List<Material>>(emptyList())
	private val _relatedPointOfInterest = MutableStateFlow<List<PointOfInterest>>(emptyList())
	private val _relatedTrees = MutableStateFlow<List<Tree>>(emptyList())
	private val _isLoading = MutableStateFlow(true)
	private val _error = MutableStateFlow<String?>(null)


	val biomeUiState: StateFlow<BiomeDetailUiState> = combine(
		_biome,
		_mainBoss,
		_relatedCreatures,
		_relatedOreDeposits,
		_relatedMaterials,
		_relatedPointOfInterest,
		_relatedTrees,
		favoriteUseCases.isFavorite(_biomeId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		BiomeDetailUiState(
			biome = values[0] as Biome?,
			mainBoss = values[1] as MainBoss?,
			relatedCreatures = values[2] as List<Creature>,
			relatedOreDeposits = values[3] as List<OreDeposit>,
			relatedMaterials = values[4] as List<Material>,
			relatedPointOfInterest = values[5] as List<PointOfInterest>,
			relatedTrees = values[6] as List<Tree>,
			isFavorite = values[7] as Boolean,
			isLoading = values[8] as Boolean,
			error = values[9] as String?
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Companion.WhileSubscribed(5000),
		initialValue = BiomeDetailUiState()
	)


	init {
		initialBiomeData()
	}

	internal fun initialBiomeData() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.value = true
			try {

				val biomeData = biomeUseCases.getBiomeByIdUseCase(biomeId = _biomeId).firstOrNull()
				_biome.value = biomeData
				val relatedIds: List<String> = async {
					relationsUseCase.getRelatedIdsUseCase(_biomeId)
						.first()
						.map { it.id }
				}.await()

				launch {
					try {
						creaturesUseCase.getCreatureByRelationAndSubCategory(
							relatedIds,
							CreatureSubCategory.BOSS
						).first()?.toMainBoss().let { boss ->
							_mainBoss.value = boss
						}
					} catch (e: Exception) {
						Log.e("Boss fetch error BiomeDetailViewModel", e.message.toString())
						_mainBoss.value = null
					}
				}

				launch {
					try {
						_relatedCreatures.value =
							creaturesUseCase.getCreaturesByIds(relatedIds).first()
					} catch (e: Exception) {
						Log.e("Creatures fetch error BiomeDetailViewModel", e.message.toString())
					}
				}

				launch {
					try {
						_relatedOreDeposits.value =
							oreDepositUseCases.getOreDepositsByIdsUseCase(relatedIds).first()

					} catch (e: Exception) {
						Log.e("Ore deposits fetch error BiomeDetailViewModel", e.message.toString())
					}
				}
				launch {
					try {
						_relatedMaterials.value =
							materialUseCases.getMaterialsByIds(relatedIds).first()
					} catch (e: Exception) {
						Log.e("Materials fetch error BiomeDetailViewModel", e.message.toString())
					}
				}


				launch {
					try {
						_relatedPointOfInterest.value =
							pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(relatedIds)
								.first()
					} catch (e: Exception) {
						Log.e(
							"PointOfInterest fetch error BiomeDetailViewModel",
							e.message.toString()
						)
					}
				}
				launch {
					try {
						_relatedTrees.value = treeUseCases.getTreesByIdsUseCase(relatedIds).first()
					} catch (e: Exception) {
						Log.e("Trees fetch error BiomeDetailViewModel", e.message.toString())
					}
				}
				_isLoading.value = false
			} catch (e: Exception) {
				Log.e("General fetch error BiomeDetailViewModel", e.message.toString())
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