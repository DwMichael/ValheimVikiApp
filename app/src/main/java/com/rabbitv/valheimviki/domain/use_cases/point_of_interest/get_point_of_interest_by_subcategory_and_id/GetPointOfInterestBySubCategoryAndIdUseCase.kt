package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory_and_id

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetPointOfInterestBySubCategoryAndIdUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(subCategory: String, id: String): Flow<PointOfInterest?> {
        return repository.getPointOfInterestBySubCategoryAndId(subCategory, id)
            .flowOn(Dispatchers.IO)

    }
}