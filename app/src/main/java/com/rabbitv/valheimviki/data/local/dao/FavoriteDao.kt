package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

	@Query("SELECT * FROM favorite")
	fun getAllFavorites(): Flow<List<Favorite>>

	@Query("SELECT * FROM favorite where id = :id")
	fun isFavorite(id: String): Flow<Favorite?>

	@Update
	suspend fun updateFavorites(favorites: List<Favorite>)
	
	@Delete
	suspend fun deleteFavorite(favorite: Favorite)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addFavorite(favorite: Favorite)


}