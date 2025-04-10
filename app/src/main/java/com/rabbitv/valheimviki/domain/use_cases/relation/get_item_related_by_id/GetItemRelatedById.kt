package com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id

import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import jakarta.inject.Inject

class GetItemRelatedById @Inject constructor( private val relationRepository: RelationsRepository) {
    operator fun invoke(id: String): String {
        return relationRepository.getRelatedId(id)
    }
}