package com.rabbitv.valheimviki.presentation.detail.armor.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.presentation.detail.armor.model.ArmorUiState
import com.rabbitv.valheimviki.utils.Constants.ARMOR_KEY
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
@HiltViewModel
class ArmorDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val armorUseCases: ArmorUseCases,
	private val relationUseCase: RelationUseCases,
	private val materialUseCases: MaterialUseCases,
	private val craftingObjectUseCases: CraftingObjectUseCases,
	private val favoriteUseCases: FavoriteUseCases,
) : ViewModel() {
	private val _armorId: String = checkNotNull(savedStateHandle[ARMOR_KEY])
	private val _armor: MutableStateFlow<Armor?> = MutableStateFlow(null)


	private val _relatedMaterials: MutableStateFlow<List<MaterialUpgrade>> =
		MutableStateFlow(emptyList())
	private val _relatedCraftingObjects: MutableStateFlow<CraftingObject?> = MutableStateFlow(null)

	private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
	private val _error: MutableStateFlow<String?> = MutableStateFlow(null)


	val uiState: StateFlow<ArmorUiState> = combine(
		_armor,
		_relatedMaterials,
		_relatedCraftingObjects,
		favoriteUseCases.isFavorite(_armorId)
			.flowOn(Dispatchers.IO),
		_isLoading,
		_error
	) { values ->
		ArmorUiState(
			armor = values[0] as Armor?,
			materials = values[1] as List<MaterialUpgrade>,
			craftingObject = values[2] as CraftingObject?,
			isFavorite = values[3] as Boolean,
			isLoading = values[4] as Boolean,
			error = values[5] as String?,
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = ArmorUiState()
	)

	init {
		loadArmorData()
	}


	internal fun loadArmorData() {

		viewModelScope.launch(Dispatchers.IO) {
			try {
				_isLoading.value = true
				val armorDeferred = async { armorUseCases.getArmorByIdUseCase(_armorId).first() }
				val relatedObjectsDeferred =
					async { relationUseCase.getRelatedIdsUseCase(_armorId).first() }

				val armor = armorDeferred.await()
				val relatedObjects = relatedObjectsDeferred.await()

				_armor.value = armor

				val relatedIds = relatedObjects.map { it.id }

				val materialsDeferred = async {
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
				}
				val craftingObjectsDeferred = async {
					val craftingObject =
						craftingObjectUseCases.getCraftingObjectByIds(relatedIds).first()
					_relatedCraftingObjects.value = craftingObject
				}

				awaitAll(materialsDeferred, craftingObjectsDeferred)
			} catch (e: Exception) {
				Log.e("General fetch error ArmorDetailViewModel", e.message.toString())
				_isLoading.value = false
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
