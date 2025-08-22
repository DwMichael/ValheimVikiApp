package com.rabbitv.valheimviki.presentation.detail.material.seeds.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.presentation.detail.material.seeds.model.SeedUiState
import com.rabbitv.valheimviki.utils.Constants.SEED_MATERIAL_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class SeedMaterialDetailViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val biomeUseCases: BiomeUseCases,
	private val pointOfInterestUseCases: PointOfInterestUseCases,
	private val treeUseCases: TreeUseCases,
	private val toolUseCases: ToolUseCases,
	private val creatureUseCases: CreatureUseCases,
	private val relationUseCases: RelationUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val _materialId: String = checkNotNull(savedStateHandle[SEED_MATERIAL_KEY])
	private val _material = MutableStateFlow<Material?>(null)
	private val _pointsOfInterest = MutableStateFlow<List<PointOfInterest>>(emptyList())
	private val _tools = MutableStateFlow<List<ItemTool>>(emptyList())
	private val _npc = MutableStateFlow<NPC?>(null)
	private val _biomes = MutableStateFlow<List<Biome>>(emptyList())
	private val _trees = MutableStateFlow<List<Tree>>(emptyList())
	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)
	private val _isFavorite = favoriteUseCases.isFavorite(_materialId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)

	val uiState = combine(
		_material,
		_biomes,
		_pointsOfInterest,
		_npc,
		_tools,
		_trees,
		_isFavorite,
		_isLoading,
		_error
	) { values ->
		SeedUiState(
			material = values[0] as Material?,
			biomes = values[1] as List<Biome>,
			pointsOfInterest = values[2] as List<PointOfInterest>,
			npc = values[3] as NPC?,
			tools = values[4] as List<ItemTool>,
			trees = values[5] as List<Tree>,
			isFavorite = values[6] as Boolean,
			isLoading = values[7] as Boolean,
			error = values[8] as String?
		)

	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		SeedUiState()
	)

	init {
		loadMobDropData()
	}

	internal fun loadMobDropData() {

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

				val pointOfInterestDeferred = async {
					pointOfInterestUseCases.getPointsOfInterestByIdsUseCase(
						relationIds
					).first()
				}

				val npcDeferred = async {
					creatureUseCases.getCreatureByRelationAndSubCategory(
						relationIds,
						CreatureSubCategory.NPC
					).first()?.toNPC()
				}

				val toolsDeferred = async {
					toolUseCases.getToolsByIdsUseCase(
						relationIds
					).first()
				}

				val treesDeferred = async {
					treeUseCases.getTreesByIdsUseCase(
						relationIds
					).first()
				}

				val biomesDeferred = async {
					biomeUseCases.getBiomesByIdsUseCase(
						relationIds
					).first()
				}
				_biomes.value = biomesDeferred.await()
				_pointsOfInterest.value = pointOfInterestDeferred.await()
					.filter { it.subCategory == PointOfInterestSubCategory.STRUCTURE.toString() }
				_tools.value = toolsDeferred.await()
				_npc.value = npcDeferred.await()
				_trees.value = treesDeferred.await()

			} catch (e: Exception) {
				Log.e("SeedMaterialDetailViewModel", "General fetch error: ${e.message}", e)
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