package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeaponRepository {
	suspend fun fetchWeapons(lang: String): Response<List<Weapon>>
	fun getLocalWeapons(): Flow<List<Weapon>>
	fun getWeaponsByIds(ids: List<String>): Flow<List<Weapon>>
	fun getWeaponById(id: String): Flow<Weapon?>
	suspend fun insertWeapons(weapons: List<Weapon>)
}