@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.presentation.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.ui_state.ui_state.UiState
import com.rabbitv.valheimviki.domain.use_cases.favorite.FavoriteUseCases
import com.rabbitv.valheimviki.presentation.favorite.model.FavoriteCategory
import com.rabbitv.valheimviki.presentation.favorite.model.UiStateFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
	private val favoriteUseCases: FavoriteUseCases,
) : ViewModel() {

	private val _selectedCategory = MutableStateFlow<FavoriteCategory?>(null)

	val uiState: StateFlow<UiState<UiStateFavorite>> = combine(
		favoriteUseCases.getAllFavoritesUseCase(),
		_selectedCategory
	) { favorites, selectedCategory ->
		UiState.Success(
			UiStateFavorite(
				favorites = favorites.asSequence()
					.filter { selectedCategory == null || it.category == selectedCategory.toString() }
					.toList(),
				selectedCategory = selectedCategory
			)
		)
	}.flowOn(Dispatchers.IO)
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			UiState.Loading()
		)

	fun onCategorySelected(cat: FavoriteCategory?) {
		_selectedCategory.value = cat
	}
}

