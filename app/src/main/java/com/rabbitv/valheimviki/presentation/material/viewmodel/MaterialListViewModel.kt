package com.rabbitv.valheimviki.presentation.material.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.domain.model.material.MaterialSubType
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.presentation.material.model.MaterialSegmentOption
import com.rabbitv.valheimviki.presentation.material.model.MaterialUiEvent
import com.rabbitv.valheimviki.presentation.material.model.MaterialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class MaterialListViewModel @Inject constructor(
	private val materialUseCases: MaterialUseCases,
	private val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _selectedSubCategory =
		MutableStateFlow<MaterialSubCategory?>(null)
	private val _selectedSubType =
		MutableStateFlow<MaterialSubType?>(null)


	val uiState: StateFlow<MaterialUiState> =
		combine(
			materialUseCases.getLocalMaterials(),
			_selectedSubCategory,
			_selectedSubType,
			connectivityObserver.isConnected
				.stateIn(
					viewModelScope,
					SharingStarted.WhileSubscribed(5000),
					true
				)
		) { materials, selectedSubCategory, selectedSubType, isConnected ->
			val filteredMaterials =
				filterMaterialList(materials, selectedSubCategory, selectedSubType)

			when {
				filteredMaterials.isNotEmpty() -> MaterialUiState(
					selectedCategory = selectedSubCategory,
					selectedChip = selectedSubType,
					materialsUiState = UIState.Success(filteredMaterials),

					)

				isConnected -> MaterialUiState(
					selectedCategory = selectedSubCategory,
					selectedChip = selectedSubType,
					materialsUiState = UIState.Loading
				)

				else -> MaterialUiState(
					selectedCategory = selectedSubCategory,
					selectedChip = selectedSubType,
					materialsUiState = UIState.Error(error_no_connection_with_empty_list_message.toString())
				)
			}
		}.flowOn(defaultDispatcher).catch { e ->
			Log.e("MaterialListVM", "Error in uiState flow", e)
			emit(
				MaterialUiState(
					selectedCategory = _selectedSubCategory.value,
					selectedChip = _selectedSubType.value,
					materialsUiState = UIState.Error(e.message ?: "An unknown error occurred")
				)
			)
		}.stateIn(
			viewModelScope,
			SharingStarted.Companion.WhileSubscribed(5000),
			initialValue = MaterialUiState(
				selectedChip = null,
				materialsUiState = UIState.Loading
			)
		)

	private fun filterMaterialList(
		materials: List<Material>,
		category: MaterialSubCategory?,
		type: MaterialSubType?
	): List<Material> {
		if (category == null) {
			return emptyList()
		}

		if (type == null) {
			return materials.filter { it.subCategory == category.toString() }
				.sortedBy { it.order }
		}

		return materials.asSequence()
			.filter { it.subCategory == category.toString() }
			.filter { it.subType == type.toString() }
			.sortedBy { it.name }
			.toList()
	}

	fun onEvent(event: MaterialUiEvent) {
		when (event) {
			is MaterialUiEvent.CategorySelected -> _selectedSubCategory.update { event.category }
			is MaterialUiEvent.ChipSelected -> {
				if (_selectedSubType.value == event.chip) {
					_selectedSubType.update { null }
				} else {
					_selectedSubType.update { event.chip }
				}
			}
		}
	}

	fun getLabelFor(subCategory: MaterialSubCategory): String =
		MaterialSegmentOption.entries
			.first { it.value == subCategory }
			.label
}