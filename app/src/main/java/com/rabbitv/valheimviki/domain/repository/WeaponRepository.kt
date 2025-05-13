package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeaponRepository {
    suspend fun fetchWeapons(lang: String): Response<List<Weapon>>
    fun getLocalWeapons(): Flow<List<Weapon>>
    fun getWeaponsBySubCategory(subCategory: String): List<Weapon>
    suspend fun insertWeapons(weapons: List<Weapon>)
}