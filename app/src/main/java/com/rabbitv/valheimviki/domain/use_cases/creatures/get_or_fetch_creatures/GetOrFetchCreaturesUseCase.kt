package com.rabbitv.valheimviki.domain.use_cases.creatures.get_or_fetch_creatures

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrFetchCreaturesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(lang: String): Flow<List<Creature>> {

        return creatureRepository.getAllCreatures()

    }
}