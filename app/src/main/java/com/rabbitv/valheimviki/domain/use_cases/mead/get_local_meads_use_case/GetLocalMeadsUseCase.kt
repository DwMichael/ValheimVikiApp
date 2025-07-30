package com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case

import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalMeadsUseCase @Inject constructor(
	private val meadRepository: MeadRepository
) {
	operator fun invoke(): Flow<List<Mead>> = meadRepository.getLocalMeads()
}