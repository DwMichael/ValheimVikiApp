package com.rabbitv.valheimviki.presentation.building_material.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialSegmentOption
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialUiEvent
import com.rabbitv.valheimviki.presentation.building_material.model.BuildingMaterialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class BuildingMaterialListViewModel @Inject constructor(
	private val buildingMaterialUseCases: BuildMaterialUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _selectedSubCategory =
		MutableStateFlow<BuildingMaterialSubCategory?>(null)


	private val _selectedSubType =
		MutableStateFlow<BuildingMaterialSubType?>(null)

	internal val materialList: Flow<List<BuildingMaterial>> =
		combine(
			buildingMaterialUseCases.getLocalBuildMaterial(),
			_selectedSubCategory,
			_selectedSubType
		) { all, category, type ->
			if (category == null) {
				return@combine emptyList()
			}

			if (type == null) {
				return@combine all.filter { it.subCategory == category.toString() }
					.sortedBy { it.order }
			}

			all
				.filter { it.subCategory == category.toString() }
				.filter { it.subType == type.toString() }
				.sortedBy { it.order }
		}.flowOn(defaultDispatcher)
			.onCompletion { error -> println("Error -> ${error?.message}") }
			.catch { println("Caught -> ${it.message}") }


	val uiState: StateFlow<BuildingMaterialUiState> =
		combine(
			materialList,
			_selectedSubCategory,
			_selectedSubType,
			connectivityObserver.isConnected.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = false
			)
		) { materials, selectedSubCategory, selectedSubType, isConnected ->
			when {
				materials.isNotEmpty() -> BuildingMaterialUiState(
					selectedCategory = selectedSubCategory,
					selectedChip = selectedSubType,
					materialsUiState = UIState.Success(materials),

					)

				isConnected -> BuildingMaterialUiState(
					selectedCategory = selectedSubCategory,
					selectedChip = selectedSubType,
					materialsUiState = UIState.Loading
				)

				else -> BuildingMaterialUiState(
					selectedCategory = selectedSubCategory,
					selectedChip = selectedSubType,
					materialsUiState = UIState.Error(error_no_connection_with_empty_list_message.toString())
				)
			}
		}.catch { e ->
			Log.e("BuildingMaterialListVM", "Error in uiState flow", e)
			emit(
				BuildingMaterialUiState(
					selectedCategory = _selectedSubCategory.value,
					selectedChip = _selectedSubType.value,
					materialsUiState = UIState.Error(e.message ?: "An unknown error occurred")
				)
			)
		}.stateIn(
			viewModelScope,
			SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = BuildingMaterialUiState(
				selectedChip = null,
				materialsUiState = UIState.Loading
			)
		)

	fun onEvent(event: BuildingMaterialUiEvent) {
		when (event) {
			is BuildingMaterialUiEvent.CategorySelected -> _selectedSubCategory.update { event.category }
			is BuildingMaterialUiEvent.ChipSelected -> {
				if (_selectedSubType.value == event.chip) {
					_selectedSubType.update { null }
				} else {
					_selectedSubType.update { event.chip }
				}
			}
		}
	}


	fun getLabelFor(subCategory: BuildingMaterialSubCategory): String =
		BuildingMaterialSegmentOption.entries
			.first { it.value == subCategory }
			.label
}