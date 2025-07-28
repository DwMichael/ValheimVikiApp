package com.rabbitv.valheimviki.presentation.mead.model

import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory


sealed class MeadUiEvent {
	data class CategorySelected(val category: MeadSubCategory) : MeadUiEvent()
}