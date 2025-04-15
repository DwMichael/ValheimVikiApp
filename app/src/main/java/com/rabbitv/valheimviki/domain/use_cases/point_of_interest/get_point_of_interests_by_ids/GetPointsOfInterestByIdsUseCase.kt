package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids

import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject

class GetPointsOfInterestByIdsUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(ids: List<String>): List<PointOfInterest> {
        return try {
            val pointOfInterest = repository.getPointOfInterestsByIds(ids)
            if (pointOfInterest.isNotEmpty()) {
                pointOfInterest
            }else
            {
                throw PointOfInterestByIdsFetchLocalException("No point of interests found with ids $ids")
            }
        } catch (e: Exception) {
            throw PointOfInterestByIdsFetchLocalException("Error fetching from Room point of interests by ids : ${e.message}")
        }
    }
}