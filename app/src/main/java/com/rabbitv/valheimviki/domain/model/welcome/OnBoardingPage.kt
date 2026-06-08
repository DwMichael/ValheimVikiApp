package com.rabbitv.valheimviki.domain.model.welcome

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rabbitv.valheimviki.R

sealed class OnBoardingPage(
	@param:DrawableRes
	val image: Int,
	@param:StringRes
	val title: Int,
	@param:StringRes
	val description: Int,
	@param:StringRes
	val buttonTitle: Int,
) {
	object First : OnBoardingPage(
		image = R.drawable.welcome_bg,
		title = R.string.onboarding_title_app,
		description = R.string.onboarding_desc_app,
		buttonTitle = R.string.onboarding_cta_get_started
	)

	object Second : OnBoardingPage(
		image = R.drawable.welcome_bg_second,
		title = R.string.onboarding_title_explore,
		description = R.string.onboarding_desc_explore,
		buttonTitle = R.string.onboarding_cta_continue
	)

	object Third : OnBoardingPage(
		image = R.drawable.welcome_bg_third,
		title = R.string.onboarding_title_welcome,
		description = R.string.onboarding_desc_welcome,
		buttonTitle = R.string.onboarding_cta_explore_now
	)
}
