package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.creature.Creature
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface CreaturesRepository {
    fun getAllCreatures(): Flow<List<Creature>>
    fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>>
    fun getCreatureByIdAndSubCategory(id:String,subCategory: String): Creature
    fun getCreaturesByIds(ids: List<String>): List<Creature>
    fun getCreatureById(id: String): Creature?

    suspend fun insertCreatures(creatures: List<Creature>)
    suspend fun fetchCreatures(lang: String): Response<List<Creature>>
}