package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TrinketRepository {
	fun getLocalTrinkets(): Flow<List<Trinket>>
	fun getTrinketById(id: String): Flow<Trinket?>
	fun getTrinketsByIds(ids: List<String>): Flow<List<Trinket>>

	suspend fun fetchTrinkets(lang: String): Response<List<Trinket>>
	suspend fun insertTrinkets(trees: List<Trinket>)
}