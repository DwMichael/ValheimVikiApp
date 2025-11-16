package com.rabbitv.valheimviki.domain.use_cases.favorite.sync_favorite

import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class SyncFavoritesUseCase @Inject constructor(
	private val favoriteRepository: FavoriteRepository,
	private val searchRepository: SearchRepository
) {
	suspend operator fun invoke() {
		val oldFavorites = favoriteRepository.getFavorites().first()
		if (oldFavorites.isEmpty()) return

		val newSearchData = searchRepository.getSearchData().first()

		val newDataMap = newSearchData.associateBy { it.id to it.category }

		val updatedFavorites = oldFavorites.mapNotNull { oldFav ->
			val newData = newDataMap[oldFav.id to oldFav.category]

			if (newData != null) {
				oldFav.copy(
					name = newData.name,
					imageUrl = newData.imageUrl,
					description = newData.description,
					category = newData.category,
					subCategory = newData.subCategory,
				)
			} else {
				null
			}
		}

		favoriteRepository.updateFavorites(updatedFavorites)
	}
}