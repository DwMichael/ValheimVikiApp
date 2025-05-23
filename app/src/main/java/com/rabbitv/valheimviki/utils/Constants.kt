package com.rabbitv.valheimviki.utils

import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory

object Constants {


    //Languages
    const val DEFAULT_LANG = "en"
    const val PL_LANG = "pl"

    //DataSource
    const val PREFERENCES_NAME = "valheimViki_preferences"
    const val PREFERENCES_KEY = "on_boarding_completed"
    const val PREFERENCES_LANGUAGE_KEY = "language_saved"

    //GITDSIZE
    const val BIOME_GRID_COLUMNS = 2
    const val NORMAL_SIZE_GRID = 2
    const val LARGE_SIZE_GRID = 3
    const val LAST_ON_BOARDING_PAGE = 2

    //DATA NAVIGATION ARGUMENT KEYS
    const val BIOME_ARGUMENT_KEY = "biomeId"
    const val TEXT_ARGUMENT_KEY = "text"
    const val MAIN_BOSS_ARGUMENT_KEY = "mainBossId"
    const val MINI_BOSS_ARGUMENT_KEY = "miniBossId"
    const val AGGRESSIVE_CREATURE_KEY = "aggressiveCId"
    const val PASSIVE_CREATURE_KEY = "passiveCId"
    const val NPC_KEY = "npcId"

    //GROUP ROUTE NAMES
    const val MAIN_ROUTE_GRAPH = "mainGraph"
    const val DETAIL_ROUTE_GRAPH = "detailGraph"

    //Creature Order map
    val CreatureSubCategory_ORDER_MAP = mapOf(
        CreatureSubCategory.BOSS.toString() to 1,
        CreatureSubCategory.MINI_BOSS.toString() to 2,
        CreatureSubCategory.AGGRESSIVE_CREATURE.toString() to 3,
        CreatureSubCategory.PASSIVE_CREATURE.toString() to 4,
        CreatureSubCategory.NPC.toString() to 5,
    )


}
