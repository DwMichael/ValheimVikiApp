package com.rabbitv.valheimviki.presentation.detail.material.mini_boss_drop.model

import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class MiniBossDropUiState(
	val material: Material? = null,
	val miniBoss: UIState<MiniBoss?> = UIState.Loading,
	val npc: UIState<NPC?> = UIState.Loading,
	val isFavorite: Boolean = false
)
