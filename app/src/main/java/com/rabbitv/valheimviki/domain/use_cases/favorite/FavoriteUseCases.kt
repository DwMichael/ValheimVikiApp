package com.rabbitv.valheimviki.domain.use_cases.favorite

import com.rabbitv.valheimviki.domain.use_cases.favorite.add_to_favorite.AddFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.delete_from_favorite.DeleteFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.get_all_favorite_items.GetAllFavoritesUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.is_favorite.IsFavoriteUseCase

data class FavoriteUseCases(
	val isFavorite: IsFavoriteUseCase,
	val getAllFavoritesUseCase: GetAllFavoritesUseCase,
	val deleteFavoriteUseCase: DeleteFavoriteUseCase,
	val addFavoriteUseCase: AddFavoriteUseCase
)