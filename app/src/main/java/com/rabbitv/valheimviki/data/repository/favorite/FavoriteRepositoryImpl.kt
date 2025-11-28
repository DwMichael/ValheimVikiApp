package com.rabbitv.valheimviki.data.repository.favorite

import com.rabbitv.valheimviki.data.local.dao.FavoriteDao
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
	private val favoriteDao: FavoriteDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepository {
	override fun isFavorite(id: String): Flow<Favorite?> {
		return favoriteDao.isFavorite(id).flowOn(ioDispatcher)
	}

	override fun getFavorites(): Flow<List<Favorite>> {
		return favoriteDao.getAllFavorites().flowOn(ioDispatcher)
	}

	override suspend fun deleteFavorite(favorite: Favorite) {
		return withContext(ioDispatcher) {
			favoriteDao.deleteFavorite(favorite)
		}
	}

	override suspend fun addFavorite(favorite: Favorite) {
		withContext(ioDispatcher) {
			favoriteDao.addFavorite(favorite)
		}

	}

	override suspend fun updateFavorites(favorites: List<Favorite>) {
		withContext(ioDispatcher) {
			favoriteDao.updateFavorites(favorites = favorites)
		}
	}
}