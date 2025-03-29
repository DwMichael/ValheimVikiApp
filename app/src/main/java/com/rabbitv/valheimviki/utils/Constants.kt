package com.rabbitv.valheimviki.utils

import com.rabbitv.valheimviki.domain.model.creature.Type

object Constants {
    //API URL
    const val BASE_URL = "http://192.168.1.130:8100/"

    //Languages
    const val DEFAULT_LANG = "en"
    const val PL_LANG = "pl"

    //DataSource
    const val PREFERENCES_NAME = "valheimViki_preferences"
    const val PREFERENCES_KEY = "on_boarding_completed"

    //GITDSIZE
    const val BIOME_GRID_COLUMNS = 2
    const val NORMAL_SIZE_GRID = 2
    const val LARGE_SIZE_GRID = 3
    const val LAST_ON_BOARDING_PAGE = 2

    //DATA NAVIGATION ARGUMENT KEYS
    const val BIOME_ARGUMENT_KEY = "biomeId"
    const val CREATURE_ARGUMENT_KEY = "creatureId"

    //GROUP ROUTE NAMES
    const val MAIN_ROUTE_GRAPH = "mainGraph"
    const val DETAIL_ROUTE_GRAPH = "detailGraph"

    //Creature Order map
    val TYPE_ORDER_MAP = mapOf(
        Type.BOSS.toString() to 1,
        Type.MINI_BOSS.toString() to 2,
        Type.AGGRESSIVE_CREATURE.toString() to 3,
        Type.PASSIVE_CREATURE.toString() to 4,
        Type.NPC.toString() to 5,
    )


}