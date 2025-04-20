package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory

import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestBySubCategoryFetchLocalException
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject

class GetPointsOfInterestBySubCategoryUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(subCategory: PointOfInterestSubCategory): List<PointOfInterest> {
        return try {
            val pointOfInterest = repository.getPointOfInterestBySubCategory(subCategory.toString())
            if (pointOfInterest.isNotEmpty()) {
                pointOfInterest
            }else
            {
                throw PointOfInterestBySubCategoryFetchLocalException("No point of interests found with subCategory $subCategory")
            }
        } catch (e: Exception) {
            throw PointOfInterestBySubCategoryFetchLocalException("Error fetching from Room point of interests by subCategory : ${e.message}")
        }
    }
}