package com.rabbitv.valheimviki.domain.model.creature

import kotlinx.serialization.Serializable
@Serializable
enum class CreatureType {
    AGGRESSIVE_CREATURE,
    BOSS,
    MINI_BOSS,
    NPC,
    PASSIVE_CREATURE
}

