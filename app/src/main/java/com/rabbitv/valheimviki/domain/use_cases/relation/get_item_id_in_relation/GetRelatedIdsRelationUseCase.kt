package com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation

import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import jakarta.inject.Inject

class GetRelatedIdsRelationUseCase@Inject constructor(private val relationRepository: RelationsRepository) {
    operator fun invoke(itemId: String): List<String> {
       return relationRepository.getRelatedIds(itemId)
    }
}