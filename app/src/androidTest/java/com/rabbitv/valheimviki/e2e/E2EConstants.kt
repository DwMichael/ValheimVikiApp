package com.rabbitv.valheimviki.e2e

/** All testTag strings referenced by E2E tests. Single source of truth. */
object E2ETestTags {
	const val WELCOME_SCREEN = "WelcomeScreen"
	const val ONBOARDING_NEXT = "OnboardingNextButton"
	const val BIOME_SURFACE = "BiomeSurface"
	const val GRID_ITEM_PREFIX = "GridItem_"
	const val BIOME_DETAIL_SCREEN = "BiomeDetailScreen"
	const val DETAIL_BACK = "DetailBackButton"
	const val FAVORITE_TOGGLE = "FavoriteToggle"
	const val FAVORITE_GRID_SCAFFOLD = "FavoriteGridScaffold"
	const val FAVORITE_ITEM_PREFIX = "FavoriteItem_"
	const val GRID_ITEM_IMAGE_PREFIX = "GridItemImage_"
	const val FAVORITE_ITEM_IMAGE_PREFIX = "FavoriteItemImage_"
	const val DETAIL_IMAGE_PREFIX = "DetailImage_"
	const val LIST_ITEM_PREFIX = "ListItem_"
	const val DRAWER_ITEM_PREFIX = "DrawerItem_"
	const val NAV_MENU = "nav_menu"
	const val NAV_FAVORITES = "nav_favorites"
	const val NAV_SEARCH = "nav_search"
	const val NAV_SETTINGS = "nav_settings"
	const val SEARCH_INPUT = "SearchInput"
	const val LANGUAGE_NOTIFICATION_DIALOG = "LanguageNotificationDialog"
	const val LANGUAGE_NOTIFICATION_DISMISS = "LanguageNotificationDialogTestTag"
	const val LANGUAGE_SETTINGS_HIGHLIGHT_BLOCKER = "LanguageSettingsHighlightBlocker"
	const val SETTINGS_LIST_SCAFFOLD = "SettingsListScaffold"
	const val SETTINGS_TUTORIAL_OVERLAY = "SettingsTutorialOverlay"
	const val SETTINGS_TUTORIAL_NEXT = "TutorialNextButton"
	const val SIMPLE_TOP_BAR_BACK = "SimpleTopBarBackButton"

	fun gridItem(id: String): String = "$GRID_ITEM_PREFIX$id"
	fun favoriteItem(id: String): String = "$FAVORITE_ITEM_PREFIX$id"
	fun gridItemImage(id: String): String = "$GRID_ITEM_IMAGE_PREFIX$id"
	fun favoriteItemImage(id: String): String = "$FAVORITE_ITEM_IMAGE_PREFIX$id"
	fun detailImage(id: String): String = "$DETAIL_IMAGE_PREFIX$id"
	fun listItem(id: String): String = "$LIST_ITEM_PREFIX$id"
	fun drawerItem(destinationSimpleName: String): String = "$DRAWER_ITEM_PREFIX$destinationSimpleName"
}

/** Wait timeouts (ms) used by page objects and assertions. */
object E2ETimeouts {
	const val SHORT_MS = 5_000L
	const val MEDIUM_MS = 10_000L
	const val LONG_MS = 15_000L
	const val VERY_LONG_MS = 30_000L
}

/** MockWebServer fixture filenames (relative to `app/src/androidTest/assets/fixtures/`). */
object E2EFixtures {
	const val BIOMES_V1 = "biomes_v1_full_en.json"
	const val BIOMES_V2 = "biomes_v2_full_en.json"

	/** Marker present only in BIOMES_V2's biome-1 description; used to verify delta refetch. */
	const val V2_DELTA_MARKER = "[v2_updated]"
}

/** Biome ids used as test favorites. */
object E2EBiomes {
	val FIVE_FAVORITE_IDS = listOf("biome-1", "biome-2", "biome-3", "biome-4", "biome-5")
	const val MODIFIED_BIOME_ID = "biome-1"
}

/** Stable ids from the local Docker production snapshot. */
object E2ERealData {
	const val MEADOWS_ID = "35091fd8-19e1-4dfb-a3e3-c5045af10752"
	const val MEADOWS_DESCRIPTION_TEXT_ID = "2b95337a-2c34-4838-b646-55c16d04753a"
	const val MEADOWS_NAME = "Meadows"
	const val MEADOWS_DESCRIPTION_FRAGMENT = "serene and welcoming landscapes"
	const val BLACK_FOREST_ID = "7004c35d-f2cb-4325-8480-8f65ce76a05f"
	const val GREYDWARF_ID = "fa7bc2b4-1237-422f-987d-085538295b80"
	const val GREYDWARF_NAME = "Greydwarf"
	const val GREYDWARF_DESCRIPTION_FRAGMENT = "inhabit the Black Forest"
	const val EIKTHYR_ALTAR_ID = "6eca9da0-bba5-4ba2-84a6-2cf085a447ae"
	const val EIKTHYR_ALTAR_NAME = "Eithykr's Mystical altar"
	const val EIKTHYR_ALTAR_DESCRIPTION_FRAGMENT = "Forsaken Altar"
	const val DELTA_MARKER = "[e2e_updated]"
}
