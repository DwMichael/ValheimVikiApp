package com.rabbitv.valheimviki.presentation.detail.material.mob_drop.model

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material


data class MobDropUiState(
	val material: Material? = null,
	val creatures: List<Creature> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null,
)