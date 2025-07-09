package com.rabbitv.valheimviki.presentation.detail.creature.main_boss_screen.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest

data class MainBossDetailUiState(
	val mainBoss: MainBoss? = null,
	val relatedForsakenAltar: PointOfInterest? = null,
	val sacrificialStones: PointOfInterest? = null,
	val dropItems: List<Material> = emptyList(),
	val relatedSummoningItems: List<Material> = emptyList(),
	val relatedBiome: Biome? = null,
	val trophy: Material? = null,
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)