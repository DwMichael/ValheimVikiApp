package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.armor.Armor
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ArmorRepository {
    suspend fun fetchArmor(lang: String): Response<List<Armor>>
    fun getLocalArmors(): Flow<List<Armor>>
    fun getArmorsBySubCategory(subCategory: String): List<Armor>
    suspend fun insertArmors(armors: List<Armor>)
}