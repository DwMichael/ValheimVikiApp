package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory

import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestBySubCategoryFetchLocalException
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPointsOfInterestBySubCategoryUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(subCategory: PointOfInterestSubCategory): Flow<List<PointOfInterest>> {
        return repository.getPointOfInterestBySubCategory(subCategory.toString())
    }
}