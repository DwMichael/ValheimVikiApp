package com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.model.relation.RelationType
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
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingDetailUiState
import com.rabbitv.valheimviki.presentation.detail.crafting.model.CraftingProducts
import com.rabbitv.valheimviki.utils.Constants.CRAFTING_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class CraftingDetailViewModel @Inject constructor(
	private val _craftingObjectUseCases: CraftingObjectUseCases,
	private val _relationUseCase: RelationUseCases,
	private val _foodUseCase: FoodUseCases,
	private val _meadUseCase: MeadUseCases,
	private val _materialUseCase: MaterialUseCases,
	private val _weaponUseCase: WeaponUseCases,
	private val _armorUseCase: ArmorUseCases,
	private val _toolsUseCase: ToolUseCases,
	private val _buildIngMaterials: BuildMaterialUseCases,
	private val favoriteUseCases: FavoriteUseCases,
	private val _savedStateHandle: SavedStateHandle,
) : ViewModel() {
	//TODO MUST FIND THE WAY TO OPTIMALIZE IT
	private val _craftingObjectId: String = checkNotNull(_savedStateHandle[CRAFTING_KEY])
	private val _craftingObject = MutableStateFlow<CraftingObject?>(null)
	private val _craftingUpgraderObjects = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingFoodProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingMeadProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingMaterialToBuild = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingMaterialRequired = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingMaterialProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingWeaponProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingArmorProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingToolProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingBuildingMaterialProducts =
		MutableStateFlow<List<CraftingProducts>>(emptyList())

	private val _isLoading = MutableStateFlow<Boolean>(true)
	private val _error = MutableStateFlow<String?>(null)

	val uiState: StateFlow<CraftingDetailUiState> = combine(
		_craftingObject,
		_craftingUpgraderObjects,
		_craftingFoodProducts,
		_craftingMeadProducts,
		_craftingMaterialProducts,
		_craftingMaterialToBuild,
		_craftingMaterialRequired,
		_craftingWeaponProducts,
		_craftingArmorProducts,
		_craftingToolProducts,
		_craftingBuildingMaterialProducts,
		favoriteUseCases.isFavorite(_craftingObjectId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		CraftingDetailUiState(
			craftingObject = values[0] as CraftingObject?,
			craftingUpgraderObjects = values[1] as List<CraftingProducts>,
			craftingFoodProducts = values[2] as List<CraftingProducts>,
			craftingMeadProducts = values[3] as List<CraftingProducts>,
			craftingMaterialProducts = values[4] as List<CraftingProducts>,
			craftingMaterialToBuild = values[5] as List<CraftingProducts>,
			craftingMaterialRequired = values[6] as List<CraftingProducts>,
			craftingWeaponProducts = values[7] as List<CraftingProducts>,
			craftingArmorProducts = values[8] as List<CraftingProducts>,
			craftingToolProducts = values[9] as List<CraftingProducts>,
			craftingBuildingMaterialProducts = values[10] as List<CraftingProducts>,
			isFavorite = values[11] as Boolean,
			isLoading = values[12] as Boolean,
			error = values[13] as String?
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = CraftingDetailUiState(isLoading = true)
	)

	init {

		loadEssentialData()

		loadRemainingData()
	}

	private fun loadEssentialData() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				_craftingObject.value =
					_craftingObjectUseCases.getCraftingObjectById(_craftingObjectId).first()


				withContext(Dispatchers.Main) {
					_isLoading.value = false
				}
			} catch (e: Exception) {
				_error.value = e.message
				_isLoading.value = false
			}
		}
	}

	private fun loadRemainingData() {
		viewModelScope.launch(Dispatchers.IO) {
			try {

				delay(300)

				val relationObjects: List<RelatedItem> =
					_relationUseCase.getRelatedIdsUseCase(_craftingObjectId).first()

				if (relationObjects.isEmpty()) {
					return@launch
				}

				val relatedIds: List<String> = relationObjects.map { it.id }
				val relatedItemsMap = relationObjects.associateBy { it.id }


				loadCraftingUpgraders(relatedIds, relatedItemsMap)
				loadConsumables(relatedIds, relatedItemsMap)
				loadEquipment(relatedIds, relatedItemsMap)
				loadMaterials(relatedIds, relatedItemsMap)

			} catch (e: Exception) {
				_error.value = e.message
			}
		}
	}

	private suspend fun loadCraftingUpgraders(
		relatedIds: List<String>,
		relatedItemsMap: Map<String, RelatedItem>
	) {
		val craftingObjects = _craftingObjectUseCases.getCraftingObjectsByIds(relatedIds).first()
		if (craftingObjects.isNotEmpty()) {
			val tempList = craftingObjects.map { craftingObject ->
				val relatedItem = relatedItemsMap[craftingObject.id]
				CraftingProducts(
					itemDrop = craftingObject,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.CRAFTING_OBJECT,
				)
			}
			_craftingUpgraderObjects.value = tempList
		}
	}

	private suspend fun loadConsumables(
		relatedIds: List<String>,
		relatedItemsMap: Map<String, RelatedItem>
	) = withContext(Dispatchers.IO) {

		val foodDeferred = async {
			val foods = _foodUseCase.getFoodListByIdsUseCase(relatedIds).first()
			foods.map { food ->
				val relatedItem = relatedItemsMap[food.id]
				CraftingProducts(
					itemDrop = food,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.FOOD,
				)
			}
		}

		val meadDeferred = async {
			val meads = _meadUseCase.getMeadsByIdsUseCase(relatedIds).first()
			meads.map { mead ->
				val relatedItem = relatedItemsMap[mead.id]
				CraftingProducts(
					itemDrop = mead,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.MEAD,
				)
			}
		}

		_craftingFoodProducts.value = foodDeferred.await()
		_craftingMeadProducts.value = meadDeferred.await()
	}

	private suspend fun loadEquipment(
		relatedIds: List<String>,
		relatedItemsMap: Map<String, RelatedItem>
	) = withContext(Dispatchers.IO) {

		val weaponDeferred = async {
			val weapons = _weaponUseCase.getWeaponsByIdsUseCase(relatedIds).first()
			weapons.map { weapon ->
				val relatedItem = relatedItemsMap[weapon.id]
				CraftingProducts(
					itemDrop = weapon,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.WEAPON,
				)
			}
		}

		val armorDeferred = async {
			val armors = _armorUseCase.getArmorsByIdsUseCase(relatedIds).first()
			armors.map { armor ->
				val relatedItem = relatedItemsMap[armor.id]
				CraftingProducts(
					itemDrop = armor,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.ARMOR,
				)
			}
		}

		val toolDeferred = async {
			val tools = _toolsUseCase.getToolsByIdsUseCase(relatedIds).first()
			tools.map { tool ->
				val relatedItem = relatedItemsMap[tool.id]
				CraftingProducts(
					itemDrop = tool,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.TOOL,
				)
			}
		}

		_craftingWeaponProducts.value = weaponDeferred.await()
		_craftingArmorProducts.value = armorDeferred.await()
		_craftingToolProducts.value = toolDeferred.await()
	}

	private suspend fun loadMaterials(
		relatedIds: List<String>,
		relatedItemsMap: Map<String, RelatedItem>
	) = withContext(Dispatchers.IO) {

		val materialDeferred = async {
			val materials = _materialUseCase.getMaterialsByIds(relatedIds).first()

			val producedMaterials = mutableListOf<CraftingProducts>()
			val requiredMaterials = mutableListOf<CraftingProducts>()
			val buildMaterials = mutableListOf<CraftingProducts>()

			materials.forEach { material ->
				val relatedItem = relatedItemsMap[material.id]
				val quantityList = listOf(relatedItem?.quantity)

				val craftingProduct = CraftingProducts(
					itemDrop = material,
					quantityList = quantityList,
					chanceStarList = emptyList(),
					droppableType = DroppableType.MATERIAL,
				)

				when (relatedItem?.relationType) {
					RelationType.PRODUCES.name -> producedMaterials.add(craftingProduct)
					RelationType.REQUIRES.name -> requiredMaterials.add(craftingProduct)
					RelationType.TO_BUILD.name -> buildMaterials.add(craftingProduct)
				}
			}

			Triple(producedMaterials, requiredMaterials, buildMaterials)
		}

		val buildingMaterialDeferred = async {
			val buildingMaterials = _buildIngMaterials.getBuildMaterialByIds(relatedIds).first()
			buildingMaterials.map { buildingMaterial ->
				val relatedItem = relatedItemsMap[buildingMaterial.id]
				CraftingProducts(
					itemDrop = buildingMaterial,
					quantityList = listOf(relatedItem?.quantity),
					chanceStarList = emptyList(),
					droppableType = DroppableType.BUILDING_MATERIAL,
				)
			}
		}

		val (produced, required, build) = materialDeferred.await()
		_craftingMaterialProducts.value = produced
		_craftingMaterialRequired.value = required
		_craftingMaterialToBuild.value = build
		_craftingBuildingMaterialProducts.value = buildingMaterialDeferred.await()
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