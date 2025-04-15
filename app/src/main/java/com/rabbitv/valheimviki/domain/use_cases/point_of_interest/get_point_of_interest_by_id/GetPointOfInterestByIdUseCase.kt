package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_id


import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestByIdFetchLocalException
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject

class GetPointOfInterestByIdUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(id: String): PointOfInterest? {
        return try {
            repository.getPointOfInterestById(id) ?:throw PointOfInterestByIdFetchLocalException("Point of interest with id $id not found")
        }catch (e : Exception)
        {
            throw PointOfInterestByIdFetchLocalException("Error fetching from Room point of interest by id : ${e.message}")
        }
    }

}