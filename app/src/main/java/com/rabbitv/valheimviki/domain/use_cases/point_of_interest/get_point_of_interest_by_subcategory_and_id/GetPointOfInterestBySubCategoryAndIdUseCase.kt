package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory_and_id

import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestBySubCategoryAndIdFetchLocalException
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject

class GetPointOfInterestBySubCategoryAndIdUseCase@Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(subCategory: String,id:String): PointOfInterest? {
        return try {
            repository.getPointOfInterestBySubCategoryAndId(subCategory,id) ?:throw PointOfInterestBySubCategoryAndIdFetchLocalException("Point of interest with id $id and subcategory $subCategory not found")
        }catch (e : Exception)
        {
            throw PointOfInterestBySubCategoryAndIdFetchLocalException("Error fetching from Room point of interest by id : ${e.message} and subcategory $subCategory")
        }
    }
}