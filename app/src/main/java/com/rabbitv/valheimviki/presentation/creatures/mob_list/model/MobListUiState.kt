package com.rabbitv.valheimviki.presentation.creatures.mob_list.model

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory

data class MobListUiState(
    val creatureList: List<Creature> = emptyList(),
    val selectedSubCategory: CreatureSubCategory = CreatureSubCategory.PASSIVE_CREATURE,
    val isConnection: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)
