package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.relation.Relation
import retrofit2.Response
import retrofit2.http.GET



interface ApiRelationsService {
@GET("Relations")
suspend fun fetchRelations(): Response<List<Relation>>
}