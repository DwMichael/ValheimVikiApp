package com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations

import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalRelationsUseCase@Inject constructor(
    private val relationsRepository: RelationsRepository
) {

    operator fun invoke():Flow<List<Relation>>{
        return relationsRepository.getLocalRelations()
    }
}