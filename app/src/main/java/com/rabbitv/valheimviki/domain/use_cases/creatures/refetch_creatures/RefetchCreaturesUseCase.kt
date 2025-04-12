package com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures

import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RefetchCreaturesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository,
                                                  private val relationsRepository: RelationsRepository) {
    suspend operator fun invoke(language: String,  refetchUseCase: RefetchUseCases):
            Flow<List<Creature>> {

        when (refetchUseCase) {
            RefetchUseCases.GET_BOSSES -> {
                withContext(Dispatchers.IO) {
                    val response = creatureRepository.fetchCreature(language)
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody?.isNotEmpty() == true) {
                        try {
                            creatureRepository.insertCreatures(responseBody)
                        } catch (e: Exception) {
                            throw CreaturesInsertException("Insert MainBosses failed : ${e.message}")
                        }
                    } else {
                        val errorCode = response.code()
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        throw CreaturesFetchLocalException("API MainBosses request failed with code $errorCode: $errorBody")
                    }
                }
                return creatureRepository.getCreaturesBySubCategory(CreatureType.BOSS.toString())
                .map { mainBossList -> mainBossList.sortedBy { it.order } }}

            RefetchUseCases.GET_MINI_BOSSES -> {
                withContext(Dispatchers.IO) {
                    val response = creatureRepository.fetchCreature(language)
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody?.isNotEmpty() == true) {
                        try {
                            creatureRepository.insertCreatures(responseBody)
                        } catch (e: Exception) {
                            throw CreaturesInsertException("Insert MainBosses failed : ${e.message}")
                        }
                    } else {
                        val errorCode = response.code()
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        throw CreaturesFetchLocalException("API MainBosses request failed with code $errorCode: $errorBody")
                    }
                }
                return creatureRepository.getCreaturesBySubCategory(CreatureType.MINI_BOSS.toString())
                    .map { mainBossList -> mainBossList.sortedBy { it.order } }}
        }
    }
}