package com.rabbitv.valheimviki.domain.use_cases.relation.fetch_and_insert

import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import jakarta.inject.Inject

class FetchAndInsertRelationsUseCase @Inject constructor(private val repository: RelationsRepository) {
    suspend operator fun invoke(){
       repository.fetchAndInsertRelations()
   }
}