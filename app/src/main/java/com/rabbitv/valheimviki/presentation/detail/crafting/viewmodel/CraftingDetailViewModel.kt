package com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.data.mappers.favorite.toFavorite
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.RelationType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.ItemData
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.navigation.BuildingDetailDestination
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiEvent
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiState
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingProducts
import com.rabbitv.valheimviki.utils.extensions.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

private data class CraftingCtx(
	val active: Boolean,
	val materials: List<Material>,
	val relatedMap: Map<String, RelatedItem>
)

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UNCHECKED_CAST")
@HiltViewModel
class CraftingDetailViewModel @Inject constructor(
	private val _craftingObjectUseCases: CraftingObjectUseCases,
	private val _relationsUseCases: RelationUseCases,
	private val _foodUseCase: FoodUseCases,
	private val _meadUseCase: MeadUseCases,
	private val _materialUseCase: MaterialUseCases,
	private val _weaponUseCase: WeaponUseCases,
	private val _armorUseCase: ArmorUseCases,
	private val _toolsUseCase: ToolUseCases,
	private val _buildIngMaterials: BuildMaterialUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	savedStateHandle: SavedStateHandle,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val contentStart = MutableStateFlow(false)

	fun startContent() {
		contentStart.value = true
	}

	private val _craftingObjectId: String =
		savedStateHandle.toRoute<BuildingDetailDestination.CraftingObjectDetail>().craftingObjectId
	private val _craftingObject =
		_craftingObjectUseCases.getCraftingObjectById(_craftingObjectId)
			.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

	private val _isFavorite = favoriteUseCases.isFavorite(_craftingObjectId)
		.distinctUntilChanged()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = false
		)
	private val _relationObjects =
		_relationsUseCases.getRelatedIdsUseCase(_craftingObjectId).stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)

	private val relatedItemsMap = _relationObjects.filter { it.isNotEmpty() }.map { list ->
		list.associateBy { it.id }
	}.flowOn(defaultDispatcher)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyMap()
		)
	private val _relatedIds = _relationObjects.filter { it.isNotEmpty() }.map { list ->
		list.map { it.id }
	}.flowOn(defaultDispatcher)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList()
		)

	private val _craftingUpgraderObjects =
		_relatedIds.flatMapLatest { ids ->
			Log.e("FUN _craftingUpgraderObjects", "was called agian before combine")
			combine(
				_craftingObjectUseCases.getCraftingObjectsByIds(ids),
				relatedItemsMap
			) { craftingObjects, currentItemsMap ->
				Log.e("FUN _craftingUpgraderObjects", "RUN AGAIN in combine")
				craftingObjects.map { craftingObject ->
					val relatedItem = currentItemsMap[craftingObject.id]
					CraftingProducts(
						itemDrop = craftingObject,
						quantityList = listOf(relatedItem?.quantity),
						chanceStarList = emptyList(),
						droppableType = DroppableType.CRAFTING_OBJECT,
					)
				}
			}
		}.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)


	private val _craftingFoodProducts =
		_relatedIds.flatMapLatest { ids ->
			combine(
				_foodUseCase.getFoodListByIdsUseCase(ids),
				relatedItemsMap
			) { foods, currentItemsMap ->
				foods.map { food ->
					val relatedItem = currentItemsMap[food.id]
					CraftingProducts(
						itemDrop = food,
						quantityList = listOf(relatedItem?.quantity),
						chanceStarList = emptyList(),
						droppableType = DroppableType.FOOD,
					)
				}
			}
		}.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	private val _craftingMeadProducts =
		_relatedIds.flatMapLatest { ids ->
			combine(
				_meadUseCase.getMeadsByIdsUseCase(ids),
				relatedItemsMap
			) { meads, currentItemsMap ->
				meads.map { mead ->
					val relatedItem = currentItemsMap[mead.id]
					CraftingProducts(
						itemDrop = mead,
						quantityList = listOf(relatedItem?.quantity),
						chanceStarList = emptyList(),
						droppableType = DroppableType.MEAD,
					)
				}
			}
		}.map { UIState.Success(it) }
			.flowOn(defaultDispatcher)

	private val craftingCtx: SharedFlow<CraftingCtx> =
		_relatedIds
			.flatMapLatest { ids ->
				combine(
					_materialUseCase.getMaterialsByIds(ids),
					relatedItemsMap
				) { materials, currentItemsMap ->
					CraftingCtx(
						active = true,
						materials = materials,
						relatedMap = currentItemsMap
					)
				}
			}
			.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), replay = 1)
	private val _craftingMaterialProducts: Flow<UIState<List<CraftingProducts>>> =
		craftingCtx.map { it.asUiState { toListFor(RelationType.PRODUCES) } }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)

	private val _craftingMaterialsRequired: Flow<UIState<List<CraftingProducts>>> =
		craftingCtx.map { it.asUiState { toListFor(RelationType.REQUIRES) } }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)

	private val _craftingBuildMaterials: Flow<UIState<List<CraftingProducts>>> =
		craftingCtx.map { it.asUiState { toListFor(RelationType.TO_BUILD) } }
			.distinctUntilChanged()
			.flowOn(defaultDispatcher)

	private val _craftingBuildingMaterialProducts = contentStart.flatMapLatest { active ->
		combine(_relatedIds, relatedItemsMap) { ids, currentItemsMap ->
			ids to currentItemsMap
		}.flatMapLatest { (ids, currentItemsMap) ->
			if (ids.isEmpty()) {
				flowOf(UIState.Success(emptyList()))
			} else {
				_buildIngMaterials.getBuildMaterialByIds(ids)
					.map { buildingMaterials ->
						buildingMaterials.map { buildingMaterial ->
							val relatedItem = currentItemsMap[buildingMaterial.id]
							CraftingProducts(
								itemDrop = buildingMaterial,
								quantityList = listOf(relatedItem?.quantity),
								chanceStarList = emptyList(),
								droppableType = DroppableType.BUILDING_MATERIAL
							)
						}
					}
					.map<List<CraftingProducts>, UIState<List<CraftingProducts>>> {
						UIState.Success(
							it
						)
					}
					.catch { e -> emit(UIState.Error(e.message ?: "Error")) }
			}
		}
	}.flowOn(defaultDispatcher)

	private val _craftingWeaponProducts: Flow<UIState<List<CraftingProducts>>> =
		combine(_relatedIds, relatedItemsMap) { ids, currentItemsMap ->
			ids to currentItemsMap
		}.flatMapLatest { (ids, currentItemsMap) ->
			if (ids.isEmpty()) {
				flowOf(UIState.Success(emptyList()))
			} else {
				_weaponUseCase.getWeaponsByIdsUseCase(ids)
					.map { weapons ->
						weapons.map { weapon ->
							val relatedItem = currentItemsMap[weapon.id]
							CraftingProducts(
								itemDrop = weapon,
								quantityList = listOf(relatedItem?.quantity),
								chanceStarList = emptyList(),
								droppableType = DroppableType.WEAPON,
							)
						}
					}.map<List<CraftingProducts>, UIState<List<CraftingProducts>>> {
						UIState.Success(
							it
						)
					}.catch { e -> emit(UIState.Error(e.message ?: "Error")) }
			}
		}.flowOn(defaultDispatcher)

	private val _craftingArmorProducts: Flow<UIState<List<CraftingProducts>>> =
		combine(_relatedIds, relatedItemsMap) { ids, currentItemsMap ->
			ids to currentItemsMap
		}.flatMapLatest { (ids, currentItemsMap) ->
			if (ids.isEmpty()) {
				flowOf(UIState.Success(emptyList()))
			} else {
				_armorUseCase.getArmorsByIdsUseCase(ids)
					.map { weapons ->
						weapons.map { armor ->
							val relatedItem = currentItemsMap[armor.id]
							CraftingProducts(
								itemDrop = armor,
								quantityList = listOf(relatedItem?.quantity),
								chanceStarList = emptyList(),
								droppableType = DroppableType.ARMOR,
							)
						}
					}.map<List<CraftingProducts>, UIState<List<CraftingProducts>>> {
						UIState.Success(
							it
						)
					}.catch { e -> emit(UIState.Error(e.message ?: "Error")) }
			}
		}.flowOn(defaultDispatcher)


	private val _craftingToolProducts: Flow<UIState<List<CraftingProducts>>> =
		combine(_relatedIds, relatedItemsMap) { ids, currentItemsMap ->
			ids to currentItemsMap
		}.flatMapLatest { (ids, currentItemsMap) ->
			if (ids.isEmpty()) {
				flowOf(UIState.Success(emptyList()))
			} else {
				_toolsUseCase.getToolsByIdsUseCase(ids)
					.map { tools ->
						tools.map { tool ->
							val relatedItem = currentItemsMap[tool.id]
							CraftingProducts(
								itemDrop = tool,
								quantityList = listOf(relatedItem?.quantity),
								chanceStarList = emptyList(),
								droppableType = DroppableType.TOOL,
							)
						}
					}.map<List<CraftingProducts>, UIState<List<CraftingProducts>>> {
						UIState.Success(
							it
						)
					}.catch { e -> emit(UIState.Error(e.message ?: "Error")) }
			}
		}.flowOn(defaultDispatcher)


	val uiState: StateFlow<CraftingDetailUiState> = combine(
		_craftingObject,
		_craftingUpgraderObjects,
		_craftingFoodProducts,
		_craftingMeadProducts,
		_craftingMaterialProducts,
		_craftingBuildMaterials,
		_craftingMaterialsRequired,
		_craftingWeaponProducts,
		_craftingArmorProducts,
		_craftingToolProducts,
		_craftingBuildingMaterialProducts,
		_isFavorite,
	) { cO, cUO, cFP, cMeadP, cMaterialP, cBMaterial, cMaterialR, cWP, cAP, cTP, cBMaterialP, favorite ->
		CraftingDetailUiState(
			craftingObject = cO,
			craftingUpgraderObjects = cUO,
			craftingFoodProducts = cFP,
			craftingMeadProducts = cMeadP,
			craftingMaterialProducts = cMaterialP,
			craftingMaterialToBuild = cBMaterial,
			craftingMaterialRequired = cMaterialR,
			craftingWeaponProducts = cWP,
			craftingArmorProducts = cAP,
			craftingToolProducts = cTP,
			craftingBuildingMaterialProducts = cBMaterialP,
			isFavorite = favorite
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = CraftingDetailUiState()
	)


	fun uiEvent(event: CraftingDetailUiEvent) {
		when (event) {
			CraftingDetailUiEvent.ToggleFavorite -> {
				viewModelScope.launch {
					_craftingObject.value?.let { bM ->
						favoriteUseCases.toggleFavoriteUseCase(
							bM.toFavorite(),
							shouldBeFavorite = !_isFavorite.value
						)
					}
				}
			}
		}

	}
}

private fun <T : ItemData> buildCraftingProducts(
	itemData: T,
	rel: RelatedItem,
	droppableType: DroppableType = DroppableType.MATERIAL
): CraftingProducts {
	return CraftingProducts(
		itemDrop = itemData,
		quantityList = listOf(
			rel.quantity,
			rel.quantity2star,
			rel.quantity3star,
			rel.quantity4star
		),
		chanceStarList = emptyList(),
		droppableType = droppableType
	)
}

private fun <T> CraftingCtx.asUiState(build: CraftingCtx.() -> T): UIState<T> =
	if (!active) UIState.Loading else UIState.Success(build())

private fun CraftingCtx.toListFor(type: RelationType): List<CraftingProducts> =
	materials.mapNotNull { material ->
		val rel = relatedMap[material.id] ?: return@mapNotNull null
		if (rel.relationType != type.name) return@mapNotNull null
		buildCraftingProducts(material, rel, DroppableType.MATERIAL)
	}