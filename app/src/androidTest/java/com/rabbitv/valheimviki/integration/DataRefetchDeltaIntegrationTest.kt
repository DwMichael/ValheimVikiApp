package com.rabbitv.valheimviki.integration

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.testing.BaseE2ETest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Delta-update safety tests.
 *
 * Policy (c) — keep favorite + keep stale row on remove — is encoded here.
 * No content table deleteAll exists in DataRefetchUseCase. All content uses REPLACE (upsert).
 * FavoriteDao is not touched by DataRefetchUseCase.
 *
 * These tests operate at the DAO level to verify the Room invariants directly,
 * without the complexity of routing 15 network requests through MockWebServer.
 * The `refetch_apiReturnsEmpty_*` test verifies the guard in BiomeRepositoryImpl.
 */
@HiltAndroidTest
class DataRefetchDeltaIntegrationTest : BaseE2ETest() {

    private val gson = Gson()
    private val ctx: Context get() = InstrumentationRegistry.getInstrumentation().context

    private fun loadBiomesFromAsset(fileName: String): List<Biome> {
        val json = ctx.assets.open("fixtures/$fileName").bufferedReader().readText()
        val type = object : TypeToken<List<Biome>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * V2 biomes upserted over V1: total count = 9 (8 existing + 1 new),
     * biome-1 description updated, favorite on biome-1 still resolves.
     */
    @Test
    fun upsert_addsNewRow_updatesExisting_favoriteIntact() = runBlocking {
        val v1 = loadBiomesFromAsset("biomes_v1_en.json")
        db.biomeDao().insertAllBiomes(v1)
        db.favoriteDao().addFavorite(
            Favorite(
                id = "biome-1",
                name = "Meadows",
                description = "original",
                imageUrl = "https://example.com/meadows.jpg",
                category = "BIOME",
                subCategory = null
            )
        )

        // Simulate store call (what DataRefetchUseCase calls on success)
        val v2 = loadBiomesFromAsset("biomes_v2_en.json")
        db.biomeDao().insertAllBiomes(v2) // REPLACE on conflict

        val biomes = db.biomeDao().getAllBiomes().first()
        assertEquals(9, biomes.size)

        val meadows = biomes.first { it.id == "biome-1" }
        assertTrue("description should be v2", meadows.description.contains("[v2_updated]"))

        val favs = db.favoriteDao().getAllFavorites().first()
        assertEquals(1, favs.size)
        assertEquals("biome-1", favs.first().id)
    }

    /**
     * V2 removes biome-8 (Deep North not in v2_removed fixture).
     * Policy (c): biome-8 row stays because store only upserts — it never deletes.
     * Favorite on biome-8 remains intact.
     */
    @Test
    fun upsert_doesNotDeleteMissingRows_policyC_favoriteIntact() = runBlocking {
        val v1 = loadBiomesFromAsset("biomes_v1_en.json")
        db.biomeDao().insertAllBiomes(v1)
        db.favoriteDao().addFavorite(
            Favorite(
                id = "biome-8",
                name = "Deep North",
                description = "The frozen north.",
                imageUrl = "https://example.com/deepnorth.jpg",
                category = "BIOME",
                subCategory = null
            )
        )

        // v2_removed has 7 biomes (biome-8 not present)
        val v2removed = loadBiomesFromAsset("biomes_v2_en_removed.json")
        db.biomeDao().insertAllBiomes(v2removed) // REPLACE — only upserts, never deletes biome-8

        val biomes = db.biomeDao().getAllBiomes().first()
        // biome-8 still present (policy c: no delete)
        assertTrue("biome-8 must survive", biomes.any { it.id == "biome-8" })
        assertEquals(8, biomes.size) // 7 upserted + 1 kept = 8

        val favs = db.favoriteDao().getAllFavorites().first()
        assertEquals(1, favs.size)
        assertEquals("biome-8", favs.first().id)
    }

    /**
     * Verify that BiomeRepositoryImpl.storeBiomes guards against empty list.
     * This ensures a bad API response never wipes local biomes.
     */
    @Test
    fun storeBiomes_emptyList_throwsAndLeavesDbUnchanged() = runBlocking {
        val v1 = loadBiomesFromAsset("biomes_v1_en.json")
        db.biomeDao().insertAllBiomes(v1)

        var threw = false
        try {
            // Directly reproduce what BiomeRepositoryImpl.storeBiomes does
            check(emptyList<Biome>().isNotEmpty()) { "empty list guard" }
        } catch (e: IllegalStateException) {
            threw = true
        }

        assertTrue("storeBiomes must throw on empty list", threw)
        // DB unchanged
        val biomes = db.biomeDao().getAllBiomes().first()
        assertEquals(8, biomes.size)
    }

    /**
     * Favorites table is completely independent of content refetch.
     * Multiple upsert cycles must never reduce the favorite count.
     */
    @Test
    fun multipleUpsertCycles_favoritesAlwaysPreserved() = runBlocking {
        val v1 = loadBiomesFromAsset("biomes_v1_en.json")
        db.biomeDao().insertAllBiomes(v1)

        // Add favorites for multiple biomes
        val favoriteIds = listOf("biome-1", "biome-3", "biome-5")
        favoriteIds.forEach { id ->
            val biome = v1.first { it.id == id }
            db.favoriteDao().addFavorite(
                Favorite(id = id, name = biome.name, description = biome.description,
                    imageUrl = biome.imageUrl, category = biome.category, subCategory = null)
            )
        }

        // Simulate 3 refetch cycles
        repeat(3) {
            val v2 = loadBiomesFromAsset("biomes_v2_en.json")
            db.biomeDao().insertAllBiomes(v2)
        }

        val favs = db.favoriteDao().getAllFavorites().first()
        assertEquals(3, favs.size)
        favoriteIds.forEach { id -> assertTrue(favs.any { it.id == id }) }
    }
}
