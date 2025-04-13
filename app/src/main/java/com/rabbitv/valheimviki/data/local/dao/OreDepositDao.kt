package com.rabbitv.valheimviki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import kotlinx.coroutines.flow.Flow

@Dao
interface OreDepositDao {
    @Query("SELECT * FROM ore_deposits ")
    fun getLocalOreDeposits(): Flow<List<OreDeposit>>

    @Query("SELECT * FROM ore_deposits WHERE id IN (:ids)")
    fun getOreDepositsByIds(ids: List<String>): List<OreDeposit>

    @Query("SELECT * FROM ore_deposits WHERE id = :id")
    fun getOreDepositById(id: String): OreDeposit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOreDeposit(creatures: List<OreDeposit>)

}