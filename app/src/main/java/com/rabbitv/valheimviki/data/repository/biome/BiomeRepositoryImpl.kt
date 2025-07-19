package com.rabbitv.valheimviki.data.repository.biome

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class BiomeRepositoryImpl @Inject constructor(
	private val apiService: ApiBiomeService,
	private val biomeDao: BiomeDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BiomeRepository {

	override fun getLocalBiomes(): Flow<List<Biome>> {
		return biomeDao.getAllBiomes().flowOn(ioDispatcher)
	}

	override fun getBiomesByIds(ids: List<String>): Flow<List<Biome>> {
		return biomeDao.getBiomesByIds(ids).flowOn(ioDispatcher)
	}

	override fun getBiomeById(biomeId: String): Flow<Biome?> {
		return biomeDao.getBiomeById(biomeId).flowOn(ioDispatcher)
	}

	override suspend fun fetchBiomes(lang: String): Response<List<Biome>> {
		return withContext(ioDispatcher) {
			apiService.fetchBiomes(lang)
		}

	}

	override suspend fun storeBiomes(biomes: List<Biome>) {
		check(biomes.isNotEmpty()) { "Biome list cannot be empty , cannot insert ${biomes.size} biomes" }
		withContext(ioDispatcher) {
			biomeDao.insertAllBiomes(biomes)
		}
	}
}