package com.rabbitv.valheimviki.presentation.creatures.mob_list.model

import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class MobSegmentOption(
    override val label: String,
    override val value: CreatureSubCategory
) : SegmentedOption<CreatureSubCategory> {
    PASSIVE("Passive", CreatureSubCategory.PASSIVE_CREATURE),
    AGGRESSIVE("Hostile", CreatureSubCategory.AGGRESSIVE_CREATURE),
    NPC("Npc", CreatureSubCategory.NPC),
}