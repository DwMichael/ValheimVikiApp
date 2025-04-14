package com.rabbitv.valheimviki.domain.use_cases.relation.insert_relations

import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import jakarta.inject.Inject

class InsertRelationsUseCase  @Inject constructor(
    private val relationsRepository: RelationRepository
) {
    suspend operator fun invoke(relations: List<Relation>) {
        relationsRepository.insertRelations(relations)
    }
}