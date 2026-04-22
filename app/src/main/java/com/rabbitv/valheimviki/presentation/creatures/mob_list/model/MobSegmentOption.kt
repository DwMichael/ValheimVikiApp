package com.rabbitv.valheimviki.presentation.creatures.mob_list.model

import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedOption


enum class MobSegmentOption(
    override val labelRes: Int,
    override val value: CreatureSubCategory
) : SegmentedOption<CreatureSubCategory> {
    PASSIVE(com.rabbitv.valheimviki.R.string.segment_passive, CreatureSubCategory.PASSIVE_CREATURE),
    AGGRESSIVE(com.rabbitv.valheimviki.R.string.segment_hostile, CreatureSubCategory.AGGRESSIVE_CREATURE),
    NPC(com.rabbitv.valheimviki.R.string.segment_npc, CreatureSubCategory.NPC),
}