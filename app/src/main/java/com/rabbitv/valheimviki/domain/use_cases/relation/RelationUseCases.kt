package com.rabbitv.valheimviki.domain.use_cases.relation

import com.rabbitv.valheimviki.domain.use_cases.relation.fetch_and_insert.FetchAndInsertRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations.FetchRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.insert_relations.InsertRelationsUseCase

data class RelationUseCases (
    val fetchRelationsUseCase:FetchRelationsUseCase,
    val getLocalRelationsUseCase:GetLocalRelationsUseCase,
    val insertRelationsUseCase: InsertRelationsUseCase,
    val fetchAndInsertRelationsUseCase: FetchAndInsertRelationsUseCase
)