package com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_ids

import com.rabbitv.valheimviki.domain.repository.MeadRepository
import javax.inject.Inject

class GetMeadsByIdsUseCase  @Inject constructor(
	private val meadRepository: MeadRepository
){
	operator fun invoke(ids:List<String>) = meadRepository.getMeadsByIds(ids)
}