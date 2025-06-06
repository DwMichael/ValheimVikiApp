package com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations

import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetLocalRelationsUseCase @Inject constructor(
    private val relationsRepository: RelationRepository
) {
    operator fun invoke(): Flow<List<Relation>> {
        return relationsRepository.getLocalRelations().flowOn(Dispatchers.IO)
    }
}