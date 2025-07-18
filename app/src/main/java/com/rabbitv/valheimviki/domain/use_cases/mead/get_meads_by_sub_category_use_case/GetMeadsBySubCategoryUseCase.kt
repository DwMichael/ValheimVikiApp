package com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case

import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import javax.inject.Inject

class GetMeadsBySubCategoryUseCase @Inject constructor() {
	operator fun invoke(meads: List<Mead>, subCategory: MeadSubCategory): List<Mead> {
		return meads.filter { it.subCategory == subCategory.toString() }.sortedBy { it.order }
	}
}