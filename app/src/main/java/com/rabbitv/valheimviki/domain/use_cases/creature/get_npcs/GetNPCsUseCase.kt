package com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs

import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class GetNPCsUseCase @Inject constructor(private val creatureRepository: CreatureRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(language: String): Flow<List<NPC>> {
        val creatureType = CreatureType.NPC
        return creatureRepository.getCreaturesBySubCategory(creatureType.toString()).map { mainBosses -> mainBosses.toNPC().sortedBy { it.order } }
    }
}