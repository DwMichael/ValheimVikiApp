package com.rabbitv.valheimviki.presentation.detail.tree.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.MaterialDrop
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

data class TreeDetailUiState(
	val tree: Tree? = null,
	val relatedBiomes:UIState< List<Biome>> = UIState.Loading,
	val relatedMaterials:UIState< List<MaterialDrop>> = UIState.Loading,
	val relatedAxes: UIState< List<Weapon>> = UIState.Loading, // ITs the list of Axes
	val isFavorite: Boolean = false,
)