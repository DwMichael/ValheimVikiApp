package com.rabbitv.valheimviki.domain.use_cases.relation


import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id.GetItemRelatedById
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_related_ids_for.GetRelatedIdsForUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class RelationUseCases @Inject constructor(
	val getRelatedIdsForUseCase: GetRelatedIdsForUseCase,
	val getLocalRelationsUseCase: GetLocalRelationsUseCase,
	val getRelatedIdUseCase: GetItemRelatedById,
	val getRelatedIdsUseCase: GetRelatedIdsRelationUseCase,
)