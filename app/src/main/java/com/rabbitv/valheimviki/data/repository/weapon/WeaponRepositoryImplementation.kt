package com.rabbitv.valheimviki.data.repository.weapon

import com.rabbitv.valheimviki.data.local.dao.WeaponDao
import com.rabbitv.valheimviki.data.remote.api.ApiWeaponService
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class WeaponRepositoryImplementation @Inject constructor(
    private val apiService: ApiWeaponService,
    private val weaponDao: WeaponDao
) : WeaponRepository {
    override suspend fun fetchWeapons(lang: String): Response<List<Weapon>> {
        return apiService.fetchWeapons(lang)
    }

    override fun getLocalWeapons(): Flow<List<Weapon>> {
        return weaponDao.getLocalWeapons()
    }

    override suspend fun insertWeapons(weapons: List<Weapon>) {
        check(weapons.isNotEmpty()) { "Weapons list cannot be empty , cannot insert ${weapons.size} weapons" }
        weaponDao.insertWeapons(weapons)
    }
}