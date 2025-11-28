package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
	fun isFavorite(id: String): Flow<Favorite?>
	fun getFavorites(): Flow<List<Favorite>>
	suspend fun deleteFavorite(favorite: Favorite)
	suspend fun addFavorite(favorite: Favorite)
	suspend fun updateFavorites(favorites: List<Favorite>)
}