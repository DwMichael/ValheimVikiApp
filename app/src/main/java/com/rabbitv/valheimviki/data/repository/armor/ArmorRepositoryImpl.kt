package com.rabbitv.valheimviki.data.repository.armor

import com.rabbitv.valheimviki.data.local.dao.ArmorDao
import com.rabbitv.valheimviki.data.remote.api.ApiArmorService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class ArmorRepositoryImpl @Inject constructor(
	private val apiService: ApiArmorService,
	private val armorDao: ArmorDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ArmorRepository {

	override suspend fun fetchArmor(lang: String): Response<List<Armor>> {
		return withContext(ioDispatcher) {
			apiService.fetchArmor(lang)
		}
	}

	override fun getLocalArmors(): Flow<List<Armor>> {
		return armorDao.getLocalArmors().flowOn(ioDispatcher)
	}

	override fun getArmorById(id: String): Flow<Armor?> {
		return armorDao.getArmorById(id).flowOn(ioDispatcher)
	}

	override fun getArmorsByIds(ids: List<String>): Flow<List<Armor>> {
		return armorDao.getArmorsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getArmorsBySubCategory(subCategory: String): Flow<List<Armor>> {
		return armorDao.getArmorsBySubCategory(subCategory).flowOn(ioDispatcher)
	}

	override suspend fun insertArmors(armors: List<Armor>) {
		check(armors.isNotEmpty()) { "Armors list cannot be empty , cannot insert ${armors.size} armors" }
		withContext(ioDispatcher) {
			armorDao.insertArmors(armors)
		}
	}
}