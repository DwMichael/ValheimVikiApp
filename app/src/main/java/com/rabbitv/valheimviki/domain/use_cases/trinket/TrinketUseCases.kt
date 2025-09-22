package com.rabbitv.valheimviki.domain.use_cases.trinket

import com.rabbitv.valheimviki.domain.use_cases.trinket.get_local_trinkets.GetLocalTrinketsUseCase
import com.rabbitv.valheimviki.domain.use_cases.trinket.get_trinket_by_id.GetTrinketByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.trinket.get_trinkets_by_ids.GetTrinketsByIdsUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class TrinketUseCases @Inject constructor(
	val getLocalTrinketsUseCase: GetLocalTrinketsUseCase,
	val getTrinketByIdUseCase: GetTrinketByIdUseCase,
	val getTrinketsByIdsUseCase: GetTrinketsByIdsUseCase
)
