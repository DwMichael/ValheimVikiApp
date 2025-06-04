package com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.presentation.detail.creature.components.MaterialDrop
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class WeaponDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val weaponUseCases: WeaponUseCases,
    private val relationUseCase: RelationUseCases,
    private val materialUseCases: MaterialUseCases
) : ViewModel() {
    private val _weaponId: String = checkNotNull(savedStateHandle[Constants.WEAPON_KEY])
    private val _weapon: MutableStateFlow<Weapon?> = MutableStateFlow(null)
    private val _materials: MutableStateFlow<List<Material>> = MutableStateFlow(emptyList())

    private val _relatedMaterials: MutableStateFlow<List<MaterialDrop>> =
        MutableStateFlow(emptyList())

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _error: MutableStateFlow<String?> = MutableStateFlow(null)

    val uiState: StateFlow<WeaponUiState> = combine(
        _weapon,
        _materials,
        _isLoading,
        _error
    ) { weapon, materials, isLoading, error ->
        WeaponUiState(
            weapon = weapon,
            materials = materials,
            isLoading = isLoading,
            error = error
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
        try {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                launch {
                    _weapon.value = weaponUseCases.getWeaponByIdUseCase(_weaponId).first()
                }

                val relatedObjects = async {
                    relationUseCase.getRelatedIdsUseCase(_weaponId).first()
                }.await()

                val relatedIds = relatedObjects.map { it.id }


                launch {
                    try {
                        val materials = materialUseCases.getMaterialsByIds(relatedIds).first()
                        val tempList = mutableListOf<MaterialDrop>()

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
                                MaterialDrop(
                                    material = material,
                                    quantityList = quantityList,
                                    chanceStarList = emptyList(),
                                )
                            )
                        }
                        _relatedMaterials.value = tempList
                        Log.e("Upgdare Items ", "$tempList")
                    } catch (e: Exception) {
                        Log.e("WeaponDetail ViewModel", "$e")
                        _relatedMaterials.value = emptyList()
                    }
                }

            }
            _isLoading.value = false
        } catch (e: Exception) {
            Log.e("General fetch error WeaponDetailViewModel", e.message.toString())
            _isLoading.value = false
            _error.value = e.message
        }
    }

}