package com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations_and_insert

import com.rabbitv.valheimviki.domain.exceptions.RelationFetchException
import com.rabbitv.valheimviki.domain.exceptions.RelationsFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.RelationsInsertException
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first


class FetchRelationsAndInsertUseCase @Inject constructor(private val repository: RelationRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Boolean {


        val localRelations = repository.getLocalRelations().first()
        if (localRelations.isNotEmpty() && localRelations.size == 92) {
            return true
        }

        try {
                val response = repository.fetchRelations()
                val responseBody = response.body()
                if (response.isSuccessful && responseBody?.isNotEmpty() == true) {
                    try {
                        repository.insertRelations(responseBody)
                        return true
                    } catch (e: Exception) {
                        throw RelationsInsertException("Insert Relations failed : ${e.message}")
                    }
                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    throw RelationFetchException("API request failed with code $errorCode: $errorBody")
            }
        } catch (e: RelationsInsertException) {
            throw e
            return false
        } catch (e: RelationFetchException) {
            throw e
            return false
        } catch (e: Exception) {
            RelationsFetchLocalException("No local relations found ${e.message}")
            return false
        }
    }
}