package com.rabbitv.valheimviki.data.repository.weapon

import com.rabbitv.valheimviki.data.local.dao.WeaponDao
import com.rabbitv.valheimviki.data.remote.api.ApiWeaponService
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class WeaponRepositoryImplementation @Inject constructor(
	private val apiService: ApiWeaponService,
	private val weaponDao: WeaponDao,
	@param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeaponRepository {
	override suspend fun fetchWeapons(lang: String): Response<List<Weapon>> {
		return withContext(ioDispatcher) { apiService.fetchWeapons(lang) }
	}

	override fun getLocalWeapons(): Flow<List<Weapon>> {
		return weaponDao.getLocalWeapons().flowOn(ioDispatcher)
	}

	override fun getWeaponsByIds(ids: List<String>): Flow<List<Weapon>> {
		return weaponDao.getWeaponsByIds(ids).flowOn(ioDispatcher)
	}

	override fun getWeaponById(id: String): Flow<Weapon?> {
		return weaponDao.getWeaponById(id).flowOn(ioDispatcher)
	}

	override suspend fun insertWeapons(weapons: List<Weapon>) {
		check(weapons.isNotEmpty()) { "Weapons list cannot be empty , cannot insert ${weapons.size} weapons" }
		withContext(ioDispatcher) { weaponDao.insertWeapons(weapons) }
	}
}