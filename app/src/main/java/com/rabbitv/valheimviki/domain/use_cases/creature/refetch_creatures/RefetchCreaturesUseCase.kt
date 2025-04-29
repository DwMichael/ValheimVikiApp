package com.rabbitv.valheimviki.domain.use_cases.creature.refetch_creatures

import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RefetchCreaturesUseCase @Inject constructor(
    private val creatureRepository: CreatureRepository,
    private val relationsRepository: RelationRepository
) {
    suspend operator fun invoke(language: String, refetchUseCase: RefetchUseCases):
            Flow<List<Creature>> {

        when (refetchUseCase) {
            RefetchUseCases.GET_BOSSES -> {
                withContext(Dispatchers.IO) {
                    val response = creatureRepository.fetchCreatures(language)
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
                return creatureRepository.getCreaturesBySubCategory(CreatureSubCategory.BOSS.toString())
                    .map { mainBossList -> mainBossList.sortedBy { it.order } }
            }

            RefetchUseCases.GET_MINI_BOSSES -> {
                withContext(Dispatchers.IO) {
                    val response = creatureRepository.fetchCreatures(language)
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
                return creatureRepository.getCreaturesBySubCategory(CreatureSubCategory.MINI_BOSS.toString())
                    .map { mainBossList -> mainBossList.sortedBy { it.order } }
            }
        }
    }
}