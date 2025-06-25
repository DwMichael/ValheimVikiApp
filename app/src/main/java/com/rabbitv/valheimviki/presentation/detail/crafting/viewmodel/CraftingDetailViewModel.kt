package com.rabbitv.valheimviki.presentation.detail.crafting.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.presentation.DroppableType
import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
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
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
	private val _savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val _craftingObjectId: String = checkNotNull(_savedStateHandle[CRAFTING_KEY])
	private val _craftingObject = MutableStateFlow<CraftingObject?>(null)
	private val _craftingUpgraderObjects = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingFoodProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingMeadProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingMaterialProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingWeaponProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingArmorProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingToolProducts = MutableStateFlow<List<CraftingProducts>>(emptyList())
	private val _craftingBuildingMaterialProducts =
		MutableStateFlow<List<CraftingProducts>>(emptyList())

	private val _isLoading = MutableStateFlow<Boolean>(false)
	private val _error = MutableStateFlow<String?>(null)

	val uiState: StateFlow<CraftingDetailUiState> = combine(
		_craftingObject,
		_craftingUpgraderObjects,
		_craftingFoodProducts,
		_craftingMeadProducts,
		_craftingMaterialProducts,
		_craftingWeaponProducts,
		_craftingArmorProducts,
		_craftingToolProducts,
		_craftingBuildingMaterialProducts,
		_isLoading,
		_error
	) { values ->
		CraftingDetailUiState(
			craftingObject = values[0] as CraftingObject?,
			craftingUpgraderObjects = values[1] as List<CraftingProducts>,
			craftingFoodProducts = values[2] as List<CraftingProducts>,
			craftingMeadProducts = values[3] as List<CraftingProducts>,
			craftingMaterialProducts = values[4] as List<CraftingProducts>,
			craftingWeaponProducts = values[5] as List<CraftingProducts>,
			craftingArmorProducts = values[6] as List<CraftingProducts>,
			craftingToolProducts = values[7] as List<CraftingProducts>,
			craftingBuildingMaterialProducts = values[8] as List<CraftingProducts>,
			isLoading = values[9] as Boolean,
			error = values[10] as String?
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = CraftingDetailUiState()
	)

	init {
		loadCraftingData()
	}

	internal fun loadCraftingData() {
		viewModelScope.launch(Dispatchers.IO) {

			_craftingObject.value =
				_craftingObjectUseCases.getCraftingObjectById(_craftingObjectId).first()
			val relationObjects: List<RelatedItem> = async {
				_relationUseCase.getRelatedIdsUseCase(_craftingObjectId).first()
			}.await()

			val relatedIds: List<String> = relationObjects.map { it.id }


			val craftingUpgraderObjectsDeferred = async {
				val craftingObjects =
					_craftingObjectUseCases.getCraftingObjectsByIds(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				craftingObjects.forEach { craftingObject ->
					val relatedItem = relatedItemsMap[craftingObject.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = craftingObject,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.CRAFTING_OBJECT,
						)
					)
				}
				_craftingUpgraderObjects.value = tempList
			}


			val foodDeferred = async {
				val foods = _foodUseCase.getFoodListByIdsUseCase(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				foods.forEach { food ->
					val relatedItem = relatedItemsMap[food.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = food,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.FOOD,
						)
					)
				}
				_craftingFoodProducts.value = tempList
			}
			val meadDeferred = async {
				val meads = _meadUseCase.getMeadsByIdsUseCase(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				meads.forEach { mead ->
					val relatedItem = relatedItemsMap[mead.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = mead,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.MEAD,
						)
					)
				}
				_craftingMeadProducts.value = tempList
			}
			val materialDeferred = async {
				val materials = _materialUseCase.getMaterialsByIds(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				materials.forEach { material ->
					val relatedItem = relatedItemsMap[material.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = material,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.MATERIAL,
						)
					)
				}
				_craftingMaterialProducts.value = tempList
			}
			val armorDeferred = async {
				val armors = _armorUseCase.getArmorsByIdsUseCase(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				armors.forEach { armor ->
					val relatedItem = relatedItemsMap[armor.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = armor,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.ARMOR,
						)
					)
				}
				_craftingArmorProducts.value = tempList
			}

			val weaponDeferred = async {
				val weapons = _weaponUseCase.getWeaponsByIdsUseCase(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				weapons.forEach { weapon ->
					val relatedItem = relatedItemsMap[weapon.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = weapon,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.WEAPON,
						)
					)
				}
				_craftingWeaponProducts.value = tempList
			}
			val toolDeferred = async {
				val tools = _toolsUseCase.getToolsByIdsUseCase(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				tools.forEach { tool ->
					val relatedItem = relatedItemsMap[tool.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = tool,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.TOOL,
						)
					)
				}
				_craftingToolProducts.value = tempList
			}

			val buildingMaterialDeferred = async {
				val buildingMaterials = _buildIngMaterials.getBuildMaterialByIds(relatedIds).first()

				val tempList = mutableListOf<CraftingProducts>()
				val relatedItemsMap = relationObjects.associateBy { it.id }
				buildingMaterials.forEach { buildingMaterial ->
					val relatedItem = relatedItemsMap[buildingMaterial.id]
					val quantityList = listOf(
						relatedItem?.quantity,
					)
					tempList.add(
						CraftingProducts(
							itemDrop = buildingMaterial,
							quantityList = quantityList,
							chanceStarList = emptyList(),
							droppableType = DroppableType.BUILDING_MATERIAL,
						)
					)
				}
				_craftingBuildingMaterialProducts.value = tempList
			}
			awaitAll(
				craftingUpgraderObjectsDeferred,
				foodDeferred,
				meadDeferred,
				materialDeferred,
				armorDeferred,
				weaponDeferred,
				toolDeferred,
				buildingMaterialDeferred
			)
		}
	}

}