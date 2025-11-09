@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteUiEvent
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
	val favoriteUseCases: FavoriteUseCases,
	val connectivityObserver: NetworkConnectivity,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _selectedCategory = MutableStateFlow<AppCategory?>(null)

	val uiState: StateFlow<FavoriteUiState> = combine(
		flow { emitAll(favoriteUseCases.getAllFavoritesUseCase()) },
		_selectedCategory,
		connectivityObserver.isConnected.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = true
		)
	) { favorites, selectedCategory, isConnected ->
		when {
			favorites.isNotEmpty() -> {
				val processedFavorites = if (selectedCategory == null) {
					favorites.sortedBy { it.name }
				} else {
					favorites.asSequence()
						.filter { it.category == selectedCategory.toString() }
						.sortedBy { it.name }
						.toList()
				}
				FavoriteUiState(
					selectedCategory = selectedCategory,
					favoritesState = UIState.Success(processedFavorites)
				)
			}

			isConnected -> FavoriteUiState(
				selectedCategory = selectedCategory,
				favoritesState = UIState.Loading
			)

			else -> FavoriteUiState(
				selectedCategory = selectedCategory,
				favoritesState = UIState.Error(error_no_connection_with_empty_list_message.toString())
			)
		}
	}.flowOn(defaultDispatcher)
		.catch { e ->
			Log.e("FavoriteVM", "Error in uiState flow", e)
			emit(
				FavoriteUiState(
					selectedCategory = _selectedCategory.value,
					favoritesState = UIState.Error(e.message ?: "An unknown error occurred")
				)
			)
		}.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			initialValue = FavoriteUiState(
				selectedCategory = null,
				favoritesState = UIState.Loading
			)
		)

	fun onEvent(event: FavoriteUiEvent) {
		when (event) {
			is FavoriteUiEvent.CategorySelected -> {
				if (_selectedCategory.value == event.category) {
					_selectedCategory.update { null }
				} else {
					_selectedCategory.update { event.category }
				}
			}
		}
	}
}

