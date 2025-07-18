package com.rabbitv.valheimviki.presentation.armor.model

import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory

sealed class ArmorUiEvent {
	data class ChipSelected(val chip: ArmorSubCategory?) : ArmorUiEvent()
}