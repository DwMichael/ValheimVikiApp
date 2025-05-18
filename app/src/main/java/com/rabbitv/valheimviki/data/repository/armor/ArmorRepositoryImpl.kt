package com.rabbitv.valheimviki.data.repository.armor

import com.rabbitv.valheimviki.data.local.dao.ArmorDao
import com.rabbitv.valheimviki.data.remote.api.ApiArmorService
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ArmorRepositoryImpl @Inject constructor(
    private val apiService: ApiArmorService,
    private val armorDao: ArmorDao
) : ArmorRepository {
    override suspend fun fetchArmor(lang: String): Response<List<Armor>> {
        return apiService.fetchArmor(lang)
    }

    override fun getLocalArmors(): Flow<List<Armor>> {
        return armorDao.getLocalArmors()
    }

    override fun getArmorsBySubCategory(subCategory: String): List<Armor> {
        return armorDao.getArmorsBySubCategory(subCategory)
    }

    override suspend fun insertArmors(armors: List<Armor>) {
        check(armors.isNotEmpty()) { "Armors list cannot be empty , cannot insert ${armors.size} armors" }
        return armorDao.insertArmors(armors)
    }
}