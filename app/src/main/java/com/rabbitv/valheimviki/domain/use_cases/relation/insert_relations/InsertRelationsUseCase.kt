package com.rabbitv.valheimviki.domain.use_cases.relation.insert_relations

import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import jakarta.inject.Inject

class InsertRelationsUseCase  @Inject constructor(
    private val relationsRepository: RelationsRepository
) {
    suspend operator fun invoke(relations: List<Relation>) {
        relationsRepository.insertRelations(relations)
    }
}