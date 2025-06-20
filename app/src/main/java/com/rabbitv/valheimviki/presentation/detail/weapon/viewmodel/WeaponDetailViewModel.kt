package com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.food.FoodAsMaterialUpgrade
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.detail.weapon.model.ArmorUiState
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
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

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class WeaponDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val weaponUseCases: WeaponUseCases,
    private val relationUseCase: RelationUseCases,
    private val materialUseCases: MaterialUseCases,
    private val foodUseCases: FoodUseCases,
    private val craftingObjectUseCases: CraftingObjectUseCases
) : ViewModel() {
    private val _weaponId: String = checkNotNull(savedStateHandle[Constants.WEAPON_KEY])
    private val _weapon: MutableStateFlow<Weapon?> = MutableStateFlow(null)


    private val _relatedMaterials: MutableStateFlow<List<MaterialUpgrade>> =
        MutableStateFlow(emptyList())
    private val _relatedFoodAsMaterials: MutableStateFlow<List<FoodAsMaterialUpgrade>> =
        MutableStateFlow(emptyList())
    private val _relatedCraftingObjects: MutableStateFlow<CraftingObject?> =  MutableStateFlow(null)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _error: MutableStateFlow<String?> = MutableStateFlow(null)

    val uiState: StateFlow<WeaponUiState> = combine(
        _weapon,
        _relatedMaterials,
        _relatedFoodAsMaterials,
        _relatedCraftingObjects,
        _isLoading,
        _error
    ) { value ->
        WeaponUiState(
            weapon = value[0] as Weapon?,
            materials = value[1] as List<MaterialUpgrade>,
            foodAsMaterials = value[2] as List<FoodAsMaterialUpgrade>,
            craftingObjects = value[3] as CraftingObject?,
            isLoading = value[4] as Boolean,
            error = value[5] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeaponUiState()
    )

    init {
        loadWeaponData()
    }


    internal fun loadWeaponData() {

        viewModelScope.launch(Dispatchers.IO) {
                try {
                    _isLoading.value = true
                    val weaponDeferred = async { weaponUseCases.getWeaponByIdUseCase(_weaponId).first() }
                    val relatedObjectsDeferred = async { relationUseCase.getRelatedIdsUseCase(_weaponId).first() }

                    val weapon = weaponDeferred.await()
                    val relatedObjects = relatedObjectsDeferred.await()
                    _weapon.value = weapon
                    val relatedIds = relatedObjects.map { it.id }
                val materialsDeferred = async {
                    try {
                        val materials = materialUseCases.getMaterialsByIds(relatedIds).first()
                        val tempList = mutableListOf<MaterialUpgrade>()

                        val relatedItemsMap = relatedObjects.associateBy { it.id }
                        for (material in materials) {
                            val relatedItem = relatedItemsMap[material.id]
                            val quantityList = listOf<Int?>(
                                relatedItem?.quantity,
                                relatedItem?.quantity2star,
                                relatedItem?.quantity3star,
                                relatedItem?.quantity4star
                            )
                            tempList.add(
                                MaterialUpgrade(
                                    material = material,
                                    quantityList = quantityList,
                                )
                            )
                        }
                        _relatedMaterials.value = tempList
                    } catch (e: Exception) {
                        Log.e("WeaponDetail ViewModel", "$e")
                        _relatedMaterials.value = emptyList()
                    }
                }
                val foodDeferred = async {

                        val food = foodUseCases.getFoodListByIdsUseCase(relatedIds).first()
                        val tempList = mutableListOf<FoodAsMaterialUpgrade>()

                        val relatedItemsMap = relatedObjects.associateBy { it.id }
                        for (material in food) {
                            val relatedItem = relatedItemsMap[material.id]
                            val quantityList = listOf<Int?>(
                                relatedItem?.quantity,
                                relatedItem?.quantity2star,
                                relatedItem?.quantity3star,
                                relatedItem?.quantity4star
                            )
                            tempList.add(
                                FoodAsMaterialUpgrade(
                                    materialFood = material,
                                    quantityList = quantityList,
                                )
                            )
                        }
                        _relatedFoodAsMaterials.value = tempList

                }
                    val craftingObjects = async {
                      _relatedCraftingObjects.value =  craftingObjectUseCases.getCraftingObjectByIds(relatedIds).first()

                    }
                awaitAll(materialsDeferred,craftingObjects, foodDeferred)
            }catch (e: Exception) {
                    Log.e("General fetch error WeaponDetailViewModel", e.message.toString())
                    _isLoading.value = false
                    _error.value = e.message
                }finally {
                    _isLoading.value = false
                }

        }
    }

}
