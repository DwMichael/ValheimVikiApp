package com.rabbitv.valheimviki.presentation.detail.ore_deposit.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit

data class OreDepositUiState(
	val oreDeposit: OreDeposit? = null,
	val relatedBiomes: List<Biome> = emptyList(),
	val relatedMaterials: List<MaterialDrop> = emptyList(),
	val isLoading: Boolean = false,
	val error: String? = null
)