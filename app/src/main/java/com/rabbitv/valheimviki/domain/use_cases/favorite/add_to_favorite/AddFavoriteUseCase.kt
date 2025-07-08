package com.rabbitv.valheimviki.domain.use_cases.favorite.add_to_favorite

import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
	private val favoriteRepository: FavoriteRepository,
) {
	suspend operator fun invoke(favorite: Favorite): Unit = favoriteRepository.addFavorite(favorite)
}

