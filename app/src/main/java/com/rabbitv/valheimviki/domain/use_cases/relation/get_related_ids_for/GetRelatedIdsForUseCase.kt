package com.rabbitv.valheimviki.domain.use_cases.relation.get_related_ids_for

import com.rabbitv.valheimviki.domain.model.relation.RelatedItem
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRelatedIdsForUseCase @Inject constructor(
	private val relationRepository: RelationRepository
) {
	operator fun invoke(queryId: String): Flow<List<RelatedItem>> =
		relationRepository.getRelatedIdsFor(queryId)
}