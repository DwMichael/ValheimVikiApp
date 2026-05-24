package com.rabbitv.valheimviki.e2e

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

/**
 * Installs a [Dispatcher] on [MockWebServer] that responds to every endpoint
 * [DataRefetchUseCase] calls.
 *
 * Pass the asset filename for each category. If `null` for a category, that endpoint
 * returns `[]` (DataRefetchUseCase treats this as a category failure and degrades to
 * PartialSuccess — biome-only tests are still possible).
 *
 * Fixture files must live at `app/src/androidTest/assets/fixtures/<filename>.json`.
 */
class MockServerFixtures(private val server: MockWebServer) {

	private val assets get() = InstrumentationRegistry.getInstrumentation().context.assets

	/** Map of endpoint path → fixture filename or null for empty `[]`. */
	private val responses = mutableMapOf<String, String?>(
		"Biomes" to null,
		"Creatures" to null,
		"Ore_deposits" to null,
		"Materials" to null,
		"PointOfInterests" to null,
		"Trees" to null,
		"Food" to null,
		"Weapons" to null,
		"Armors" to null,
		"Mead" to null,
		"Tools" to null,
		"Trinkets" to null,
		"BuildingMaterials" to null,
		"Crafting" to null,
		"Relations" to null,
	)

	/** Override a single endpoint's payload. Filename is relative to `assets/fixtures/`. */
	fun setEndpoint(path: String, fixtureFile: String?): MockServerFixtures = apply {
		responses[path] = fixtureFile
	}

	/** Convenience: set the Biomes endpoint to a specific fixture file. */
	fun biomes(fixtureFile: String): MockServerFixtures = setEndpoint("Biomes", fixtureFile)

	fun install() {
		server.dispatcher = object : Dispatcher() {
			override fun dispatch(request: RecordedRequest): MockResponse {
				val path = request.path?.removePrefix("/")?.substringBefore("?") ?: ""
				val fixture = responses[path]
				val body = if (fixture != null) loadAsset("fixtures/$fixture") else "[]"
				return MockResponse().setResponseCode(200).setBody(body)
			}
		}
	}

	private fun loadAsset(path: String): String =
		assets.open(path).bufferedReader().use { it.readText() }
}
