package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaponDao {

    @Query("SELECT * FROM weapons")
    fun getLocalWeapons(): Flow<List<Weapon>>

    @Query("SELECT * FROM weapons where subCategory=:subCategory")
    fun getWeaponsBySubCategory(subCategory: WeaponSubCategory): Flow<List<Weapon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeapons(weapons: List<Weapon>)
}