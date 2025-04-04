package com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations

import android.util.Log
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import com.rabbitv.valheimviki.utils.bodyList
import jakarta.inject.Inject
import retrofit2.Response


class FetchRelationsUseCase @Inject constructor(
    private val relationsRepository: RelationsRepository
) {
    suspend operator fun invoke():List<Relation>{

        val response : Response<List<Relation>>  = relationsRepository.fetchRelations()

        if (response.isSuccessful)
        {
            return response.bodyList()
        }else
        {
            Log.i("Response Relation Error", response.errorBody().toString())
            return emptyList()
        }
    }
}