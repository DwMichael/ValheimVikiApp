package com.rabbitv.valheimviki.domain.use_cases.creatures.fetchCreaturesAndInsert

import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class FetchCreaturesAndInsertUseCase@Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(lang: String) = creatureRepository.fetchCreatureAndInsert(lang)
}