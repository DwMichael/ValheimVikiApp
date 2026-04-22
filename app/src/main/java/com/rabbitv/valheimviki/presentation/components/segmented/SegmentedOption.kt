package com.rabbitv.valheimviki.presentation.components.segmented

interface SegmentedOption<T> {
	@get:androidx.annotation.StringRes
	val labelRes: Int
	val value: T
}