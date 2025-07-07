package com.rabbitv.valheimviki.domain.use_cases.favorite.get_all_favorite_items

import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFavoritesUseCase @Inject constructor(
	private val favoriteRepository: FavoriteRepository,
) {
	operator fun invoke(): Flow<List<Favorite>> = favoriteRepository.getFavorites()
}