package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class MainBossDetailUiState(
	val mainBoss: MainBoss? = null,
	val relatedBiome: Biome? = null,
	val relatedForsakenAltar: PointOfInterest? = null,
	val sacrificialStones: PointOfInterest? = null,
	val dropItems: UIState<List<Material>> = UIState.Loading,
	val relatedSummoningItems: UIState<List<Material>> = UIState.Loading,
	val trophy: Material? = null,
	val isFavorite: Boolean = false,
)