package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_local_point_of_interest

import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalPointOfInterestUseCase @Inject constructor(
  private val repository: PointOfInterestRepository
) {
  operator fun invoke() :Flow<List<PointOfInterest>>{
    return repository.getLocalPointOfInterest()
  }
}