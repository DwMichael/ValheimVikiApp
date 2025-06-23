package com.rabbitv.valheimviki.domain.use_cases.mead.get_mead_by_id

import com.rabbitv.valheimviki.domain.repository.MeadRepository
import javax.inject.Inject

class GetMeadByIdUseCase @Inject constructor(
	private val meadRepository: MeadRepository
){
	operator fun invoke(id:String) = meadRepository.getMeadById(id)
}