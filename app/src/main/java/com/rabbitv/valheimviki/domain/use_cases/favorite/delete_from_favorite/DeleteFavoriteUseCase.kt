package com.rabbitv.valheimviki.domain.use_cases.favorite.delete_from_favorite

import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
	private val favoriteRepository: FavoriteRepository,
) {
	suspend fun invoke(favorite: Favorite): Unit = favoriteRepository.deleteFavorite(favorite)
}