package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface OreDepositRepository {
    fun getLocalOreDeposits(): Flow<List<OreDeposit>>
    fun getOreDepositsByIds(ids: List<String>): Flow<List<OreDeposit>>
    fun getOreDepositById(id: String): Flow<OreDeposit?>

    suspend fun insertOreDeposit(creatures: List<OreDeposit>)
    suspend fun fetchOreDeposits(lang: String): Response<List<OreDeposit>>
}