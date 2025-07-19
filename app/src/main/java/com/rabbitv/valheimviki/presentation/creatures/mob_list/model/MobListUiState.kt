package com.rabbitv.valheimviki.presentation.creatures.mob_list.model

import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.ItemData

data class MobListUiState(
	val listState: UIState<List<ItemData>> = UIState.Loading,
	val selectedSubCategory: CreatureSubCategory = CreatureSubCategory.PASSIVE_CREATURE,
)
