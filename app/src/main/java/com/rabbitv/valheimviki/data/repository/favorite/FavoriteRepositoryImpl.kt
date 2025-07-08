package com.rabbitv.valheimviki.data.repository.favorite

import com.rabbitv.valheimviki.data.local.dao.FavoriteDao
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
	private val favoriteDao: FavoriteDao
) : FavoriteRepository {
	override fun isFavorite(id: String): Flow<Favorite?> {
		return favoriteDao.isFavorite(id)
	}

	override fun getFavorites(): Flow<List<Favorite>> {
		return favoriteDao.getAllFavorites()
	}

	override suspend fun deleteFavorite(favorite: Favorite) {
		return favoriteDao.deleteFavorite(favorite)
	}

	override suspend fun addFavorite(favorite: Favorite) {
		favoriteDao.addFavorite(favorite)
	}
}