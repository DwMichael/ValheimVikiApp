package com.rabbitv.valheimviki.presentation.creatures.mob_list

import com.rabbitv.valheimviki.domain.model.creature.Creature

data class MobListUiState(
    val creatureList: List<Creature> = emptyList(),
    val selectedSubCategory: Int = 0,
    val error: String? = null,
    val isLoading: Boolean = false
)
