package com.rabbitv.valheimviki.presentation.creatures.mob_list.model

import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory

sealed class MobUiEvent {
	data class CategorySelected(val category: CreatureSubCategory) : MobUiEvent()
}