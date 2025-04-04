package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import kotlinx.coroutines.flow.Flow

interface CreaturesRepository {
    fun getCreaturesBySubCategory(subCategory: String): Flow<List<Creature>>
    fun getCreatureByIdAndSubCategory(id:String,subCategory: String): Flow<Creature>
    fun getCreaturesByIds(ids: List<String>): Flow<List<Creature>>
    fun getCreatureById(id: String): Flow<Creature>

    suspend fun fetchCreatureByType(lang: String,creatureType:CreatureType): List<Creature>

    suspend fun insertLocalCreatures(creatures: List<Creature>)
}