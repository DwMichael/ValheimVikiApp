package com.rabbitv.valheimviki.domain.use_cases.relation

import com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations.FetchRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations_and_insert.FetchRelationsAndInsertUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id.GetItemRelatedById
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.insert_relations.InsertRelationsUseCase

data class RelationUseCases (
    val fetchRelationsUseCase:FetchRelationsUseCase,
    val getLocalRelationsUseCase:GetLocalRelationsUseCase,
    val getRelatedIdUseCase: GetItemRelatedById,
    val getRelatedIdsUseCase:GetRelatedIdsRelationUseCase,
    val insertRelationsUseCase: InsertRelationsUseCase,
    val fetchRelationsAndInsertUseCase: FetchRelationsAndInsertUseCase
)