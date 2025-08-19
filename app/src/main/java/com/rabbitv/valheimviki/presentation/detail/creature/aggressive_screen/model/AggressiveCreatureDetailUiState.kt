package com.rabbitv.valheimviki.presentation.detail.creature.aggressive_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.food.FoodDrop
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class AggressiveCreatureDetailUiState(
	val aggressiveCreature: AggressiveCreature? = null,
	val biome: Biome? = null,
	val materialDrops: UIState<List<MaterialDrop>> = UIState.Loading,
	val foodDrops: UIState<List<FoodDrop>> = UIState.Loading,
	val isFavorite: Boolean = false,
)