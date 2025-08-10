package com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation

import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRelatedIdsRelationUseCase @Inject constructor(private val relationRepository: RelationRepository) {
	operator fun invoke(itemId: String): Flow<List<RelatedItem>> =
		relationRepository.getRelatedIds(itemId)

}