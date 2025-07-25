package com.rabbitv.valheimviki.presentation.detail.tree.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class TreeDetailUiState(
	val tree: Tree? = null,
	val relatedBiomes: List<Biome> = emptyList(),
	val relatedMaterials: List<MaterialDrop> = emptyList(),
	val relatedAxes: List<Weapon> = emptyList(), // ITs the list of Axes
	val isFavorite: Boolean = false,
	val isLoading: Boolean = false,
	val error: String? = null
)