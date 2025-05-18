package com.rabbitv.valheimviki.domain.use_cases.mead

import com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case.GetLocalMeadsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case.GetMeadsBySubCategoryUseCase

data class MeadUseCases(
    val getLocalMeadsUseCase: GetLocalMeadsUseCase,
    val getMeadsBySubCategoryUseCase: GetMeadsBySubCategoryUseCase,
)