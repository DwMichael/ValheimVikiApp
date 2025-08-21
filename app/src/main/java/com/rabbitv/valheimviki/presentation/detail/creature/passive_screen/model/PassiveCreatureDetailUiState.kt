package com.rabbitv.valheimviki.presentation.detail.creature.passive_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop

import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class PassiveCreatureDetailUiState(
	val passiveCreature: PassiveCreature? = null,
	val biome: Biome? = null,
	val materialDrops: UIState<List<MaterialDrop>> = UIState.Loading,
	val isFavorite: Boolean = false
)