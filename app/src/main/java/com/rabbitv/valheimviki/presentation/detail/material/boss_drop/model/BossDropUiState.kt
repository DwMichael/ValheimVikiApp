package com.rabbitv.valheimviki.presentation.detail.material.boss_drop.model

import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class BossDropUiState(
	val material: Material? = null,
	val boss: UIState<MainBoss?> = UIState.Loading,
	val isFavorite: Boolean = false
)