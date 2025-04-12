package com.rabbitv.valheimviki.domain.use_cases.creatures.get_or_fetch_creatures

import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetOrFetchCreaturesUseCase @Inject constructor(private val creatureRepository: CreaturesRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(lang: String): Flow<List<Creature>> {

        return creatureRepository.getAllCreatures()
            .flatMapLatest { localCreatures ->
                if (localCreatures.isNotEmpty()) {
                    flowOf(localCreatures)
                } else {
                    try {
                        withContext(Dispatchers.IO) {
                            val response = creatureRepository.fetchCreatures(lang)
                            val responseBody = response.body()
                            if (response.isSuccessful && responseBody?.isNotEmpty() == true) {
                                try {
                                    creatureRepository.insertCreatures(responseBody)
                                } catch (e: Exception) {
                                    throw CreaturesInsertException("Insert Creatures failed : ${e.message}")
                                }
                                creatureRepository.getAllCreatures()
                            } else {
                                val errorCode = response.code()
                                val errorBody = response.errorBody()?.string() ?: "No error body"
                                throw CreatureFetchException("API request failed with code $errorCode: $errorBody")
                            }
                        }
                    }catch (e: CreatureFetchException) {
                        throw e
                    } catch (e: CreaturesInsertException) {
                        throw e
                    } catch (e: Exception) {
                        throw CreaturesFetchLocalException("No local data available and failed to fetch from internet.")
                    }
                }
            }
    }
}