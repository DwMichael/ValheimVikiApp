package com.rabbitv.valheimviki.domain.use_cases.mead

import com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case.GetLocalMeadsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_mead_by_id.GetMeadByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_ids.GetMeadsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case.GetMeadsBySubCategoryUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class MeadUseCases @Inject constructor(
	val getLocalMeadsUseCase: GetLocalMeadsUseCase,
	val getMeadByIdUseCase: GetMeadByIdUseCase,
	val getMeadsByIdsUseCase: GetMeadsByIdsUseCase,
	val getMeadsBySubCategoryUseCase: GetMeadsBySubCategoryUseCase,
)