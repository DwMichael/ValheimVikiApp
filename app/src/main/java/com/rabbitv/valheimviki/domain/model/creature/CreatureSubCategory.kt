package com.rabbitv.valheimviki.domain.model.creature

import kotlinx.serialization.Serializable

@Serializable
enum class CreatureSubCategory {
    AGGRESSIVE_CREATURE,
    PASSIVE_CREATURE,
    NPC,
    BOSS,
    MINI_BOSS
}

