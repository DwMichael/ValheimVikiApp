package com.rabbitv.valheimviki.data.repository.ore_deposit

import com.rabbitv.valheimviki.data.local.dao.OreDepositDao
import com.rabbitv.valheimviki.data.remote.api.ApiOreDepositService
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class OreDepositRepositoryImpl @Inject constructor(
    private val apiService: ApiOreDepositService,
    private val oreDepositDao: OreDepositDao
):OreDepositRepository  {
    override fun getLocalOreDeposits(): Flow<List<OreDeposit>> {
        return oreDepositDao.getLocalOreDeposits()
    }

    override fun getOreDepositsByIds(ids: List<String>): List<OreDeposit> {
        return oreDepositDao.getOreDepositsByIds(ids)
    }

    override fun getOreDepositById(id: String): OreDeposit? {
        return oreDepositDao.getOreDepositById(id)
    }

    override suspend fun insertOreDeposit(creatures: List<OreDeposit>) {
        oreDepositDao.insertOreDeposit(creatures)
    }

    override suspend fun fetchOreDeposits(lang: String): Response<List<OreDeposit>> {
        return apiService.fetchOreDeposits(lang)
    }

}