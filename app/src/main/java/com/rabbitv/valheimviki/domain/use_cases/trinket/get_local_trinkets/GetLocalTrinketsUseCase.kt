package com.rabbitv.valheimviki.domain.use_cases.trinket.get_local_trinkets

import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.repository.TrinketRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalTrinketsUseCase @Inject constructor(
	private val trinketRepository: TrinketRepository
) {
	operator fun invoke(): Flow<List<Trinket>> = trinketRepository.getLocalTrinkets()

}
