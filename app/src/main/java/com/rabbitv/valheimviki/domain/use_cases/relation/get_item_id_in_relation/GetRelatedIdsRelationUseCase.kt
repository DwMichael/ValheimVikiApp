package com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation

import com.rabbitv.valheimviki.domain.repository.RelationRepository
import jakarta.inject.Inject

class GetRelatedIdsRelationUseCase@Inject constructor(private val relationRepository: RelationRepository) {
    operator fun invoke(itemId: String): List<String> {
       return relationRepository.getRelatedIds(itemId)
    }
}