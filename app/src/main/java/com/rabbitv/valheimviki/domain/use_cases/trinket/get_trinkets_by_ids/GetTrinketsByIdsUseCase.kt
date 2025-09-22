package com.rabbitv.valheimviki.domain.use_cases.trinket.get_trinkets_by_ids

import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.repository.TrinketRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTrinketsByIdsUseCase @Inject constructor(
	private val trinketRepository: TrinketRepository
) {
	operator fun invoke(ids: List<String>): Flow<List<Trinket>> =
		trinketRepository.getTrinketsByIds(ids)

}