package com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id

import com.rabbitv.valheimviki.domain.repository.RelationRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetItemRelatedById @Inject constructor(private val relationRepository: RelationRepository) {
    operator fun invoke(id: String): Flow<String?> {
        return relationRepository.getRelatedId(id).flowOn(Dispatchers.IO)
    }
}