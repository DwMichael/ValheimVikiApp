package com.rabbitv.valheimviki.domain.use_cases.relation


import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id.GetItemRelatedById
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase

data class RelationUseCases(
    val getLocalRelationsUseCase: GetLocalRelationsUseCase,
    val getRelatedIdUseCase: GetItemRelatedById,
    val getRelatedIdsUseCase: GetRelatedIdsRelationUseCase,
)