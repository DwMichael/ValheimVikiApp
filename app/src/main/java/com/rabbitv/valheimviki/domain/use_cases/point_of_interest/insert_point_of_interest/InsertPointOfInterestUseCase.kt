package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.insert_point_of_interest

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject

class InsertPointOfInterestUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    suspend operator fun invoke(pointOfInterest: List<PointOfInterest>) {
     repository.insertPointOfInterest(pointOfInterest)
    }
}