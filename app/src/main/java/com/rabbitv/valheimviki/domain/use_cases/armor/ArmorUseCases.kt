package com.rabbitv.valheimviki.domain.use_cases.armor

import com.rabbitv.valheimviki.domain.use_cases.armor.get_armor_by_id.GetArmorByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_ids.GetArmorsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case.GetLocalArmorsUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class ArmorUseCases @Inject constructor(
	val getLocalArmorsUseCase: GetLocalArmorsUseCase,
	val getArmorByIdUseCase: GetArmorByIdUseCase,
	val getArmorsByIdsUseCase: GetArmorsByIdsUseCase,
)
