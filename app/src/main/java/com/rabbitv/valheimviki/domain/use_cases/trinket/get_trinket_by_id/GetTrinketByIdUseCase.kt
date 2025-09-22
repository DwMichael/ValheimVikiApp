package com.rabbitv.valheimviki.domain.use_cases.trinket.get_trinket_by_id

import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.repository.TrinketRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTrinketByIdUseCase @Inject constructor(
	private val trinketRepository: TrinketRepository
) {
	operator fun invoke(id: String): Flow<Trinket?> = trinketRepository.getTrinketById(id)
}