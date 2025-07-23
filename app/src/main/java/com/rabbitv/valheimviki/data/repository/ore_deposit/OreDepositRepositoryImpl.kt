package com.rabbitv.valheimviki.data.repository.ore_deposit

import com.rabbitv.valheimviki.data.local.dao.OreDepositDao
import com.rabbitv.valheimviki.data.remote.api.ApiOreDepositService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class OreDepositRepositoryImpl @Inject constructor(
	private val apiService: ApiOreDepositService,
	private val oreDepositDao: OreDepositDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : OreDepositRepository {
	override fun getLocalOreDeposits(): Flow<List<OreDeposit>> {
		return oreDepositDao.getLocalOreDeposits().flowOn(ioDispatcher)
	}

	override fun getOreDepositsByIds(ids: List<String>): Flow<List<OreDeposit>> {
		return oreDepositDao.getOreDepositsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getOreDepositById(id: String): Flow<OreDeposit?> {
		return oreDepositDao.getOreDepositById(id).flowOn(ioDispatcher)
	}

	override suspend fun insertOreDeposit(creatures: List<OreDeposit>) {
		withContext(ioDispatcher) {
			oreDepositDao.insertOreDeposit(creatures)
		}

	}

	override suspend fun fetchOreDeposits(lang: String): Response<List<OreDeposit>> {
		return withContext(ioDispatcher) {
			apiService.fetchOreDeposits(lang)
		}
	}

}