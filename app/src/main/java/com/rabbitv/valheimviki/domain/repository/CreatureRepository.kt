package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.creature.Creature
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface CreatureRepository {
	fun getLocalCreatures(): Flow<List<Creature>>
	fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>>
	fun getCreatureByIdAndSubCategory(id: String, subCategory: String): Flow<Creature?>
	fun getCreatureByRelationAndSubCategory(
		creaturesIds: List<String>,
		subCategory: String
	): Flow<Creature?>

	fun getCreaturesByIds(ids: List<String>): Flow<List<Creature>>
	fun getCreatureById(id: String): Flow<Creature?>
	fun getCreaturesByRelationAndSubCategory(
		ids: List<String>,
		subCategory: String
	): Flow<List<Creature>>

	suspend fun insertCreatures(creatures: List<Creature>)
	suspend fun fetchCreatures(lang: String): Response<List<Creature>>
}