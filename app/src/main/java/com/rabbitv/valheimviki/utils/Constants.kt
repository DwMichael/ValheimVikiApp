package com.rabbitv.valheimviki.utils

import com.rabbitv.valheimviki.domain.model.biome.Stage
import com.rabbitv.valheimviki.domain.model.creature.Type

object Constants {

    const val BASE_URL = "http://192.168.1.130:8100/"

    const val PREFERENCES_NAME = "valheimViki_preferences"
    const val PREFERENCES_KEY = "on_boarding_completed"

    const val BIOME_GRID_COLUMNS = 2

    const val NORMAL_SIZE_GRID = 2
    const val LARGE_SIZE_GRID = 3
    const val LAST_ON_BOARDING_PAGE = 2

    const val BIOME_ARGUMENT_KEY = "biomeId"
    const val CREATURE_ARGUMENT_KEY = "creatureId"
    val STAGE_ORDER_MAP = mapOf(
        Stage.EARLY.toString() to 1,
        Stage.MID.toString() to 2,
        Stage.LATE.toString() to 3
    )

    val TYPE_ORDER_MAP = mapOf(
        Type.BOSS.toString() to 1,
        Type.MINI_BOSS.toString() to 2,
        Type.AGGRESSIVE_CREATURE.toString() to 3,
        Type.PASSIVE_CREATURE.toString() to 4,
        Type.NPC.toString() to 5,
    )
}