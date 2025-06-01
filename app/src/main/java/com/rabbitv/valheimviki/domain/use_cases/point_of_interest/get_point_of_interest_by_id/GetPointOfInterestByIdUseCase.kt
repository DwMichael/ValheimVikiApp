package com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_id


import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetPointOfInterestByIdUseCase @Inject constructor(
    private val repository: PointOfInterestRepository
) {
    operator fun invoke(id: String): Flow<PointOfInterest?> {
        return repository.getPointOfInterestById(id).flowOn(Dispatchers.IO)

    }

}