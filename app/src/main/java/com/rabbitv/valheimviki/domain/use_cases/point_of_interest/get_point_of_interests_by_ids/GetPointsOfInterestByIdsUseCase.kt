package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids

import com.rabbitv.valheimviki.domain.exceptions.PointOfInterestByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPointsOfInterestByIdsUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(ids: List<String>): Flow<List<PointOfInterest>> {
        return repository.getPointOfInterestsByIds(ids)

    }
}