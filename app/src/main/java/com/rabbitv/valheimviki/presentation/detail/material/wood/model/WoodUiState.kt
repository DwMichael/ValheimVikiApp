package com.rabbitv.valheimviki.presentation.detail.material.wood.model

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState

data class WoodUiState(
	val material: Material? = null,
	val biomes: UIState<List<Biome>> = UIState.Loading,
	val trees:  UIState<List<Tree>> = UIState.Loading,
	val isFavorite: Boolean = false
)
