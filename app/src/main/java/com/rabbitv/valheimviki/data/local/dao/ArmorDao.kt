package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.armor.Armor
import kotlinx.coroutines.flow.Flow


@Dao
interface ArmorDao {

    @Query("SELECT * FROM armors")
    fun getLocalArmors(): Flow<List<Armor>>

    @Query("SELECT * FROM armors where subCategory = :subCategory")
    fun getArmorsBySubCategory(subCategory: String): Flow<List<Armor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArmors(armors: List<Armor>)

}