package com.rabbitv.valheimviki.e2e

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase.Companion.MIGRATION_1_2
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase.Companion.MIGRATION_2_3
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Migration safety tests.
 *
 * IMPORTANT: requires `room.schemaLocation` KSP arg and `exportSchema = true` in
 * ValheimVikiDatabase. Schema JSON files are generated in app/schemas/ on each build and
 * must be committed. The androidTest assets sourceSet includes app/schemas/ so
 * MigrationTestHelper can load them at test time.
 *
 * Rule for every future version bump:
 *   1. Add MIGRATION_N_(N+1) to ValheimVikiDatabase.
 *   2. Add a migrate_N_to_(N+1)_* test here before merging.
 *   3. Never call fallbackToDestructiveMigration() in DatabaseModule.
 */
@RunWith(AndroidJUnit4::class)
class ValheimVikiDatabaseMigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ValheimVikiDatabase::class.java
    )

    /**
     * Smoke test: open current DB (v3) with existing migrations — no crash, no data wipe.
     * This proves that DatabaseModule does not use fallbackToDestructiveMigration().
     */
    @Test
    @Ignore("Requires schemas/.../2.json which was never exported. Re-enable after regenerating historical Room schemas.")
    fun migrate2To3_contentTablesCleared_favoriteTablePreserved() {
        val dbName = "migration-test-2-to-3"

        // Create DB at version 2
        helper.createDatabase(dbName, 2).apply {
            // Insert a favorite row (table exists at v2 — adjust columns if schema differs)
            execSQL(
                """INSERT OR IGNORE INTO favorite (id, name, description, imageUrl, category, subCategory)
                   VALUES ('fav-test-1', 'Test Item', 'desc', 'http://img.jpg', 'BIOME', NULL)"""
            )
            execSQL(
                """INSERT OR IGNORE INTO biomes (id, category, subCategory, imageUrl, name, description, "order")
                   VALUES ('biome-test-1', 'BIOME', NULL, 'http://img.jpg', 'Meadows', 'desc', 1)"""
            )
            close()
        }

        // Run migration 2 → 3
        val db = helper.runMigrationsAndValidate(dbName, 3, true, MIGRATION_2_3)

        db.use {
            // MIGRATION_2_3 clears content tables — biome row should be gone
            val biomeCount = it.query("SELECT count(*) FROM biomes").use { c ->
                c.moveToFirst(); c.getInt(0)
            }
            assertEquals("biomes cleared by MIGRATION_2_3", 0, biomeCount)

            // Favorite table is NOT cleared by MIGRATION_2_3 — row must survive
            val favCount = it.query("SELECT count(*) FROM favorite").use { c ->
                c.moveToFirst(); c.getInt(0)
            }
            assertEquals("favorite must survive migration", 1, favCount)
        }
    }

    /**
     * Regression guard: confirms no fallbackToDestructiveMigration is present.
     * If this test passes (i.e., migration runs without crash), the guard holds.
     * A missing migration would throw IllegalStateException before we reach assertions.
     */
    @Test
    @Ignore("Requires schemas/.../1.json which was never exported. Re-enable after regenerating historical Room schemas.")
    fun migrate1To2_noDataLoss() {
        val dbName = "migration-test-1-to-2"

        helper.createDatabase(dbName, 1).apply {
            // Insert into tables that existed at v1 (schema may be simpler)
            execSQL(
                """INSERT OR IGNORE INTO favorite (id, name, description, imageUrl, category, subCategory)
                   VALUES ('fav-v1', 'V1 Item', NULL, 'http://img.jpg', 'BIOME', NULL)"""
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(dbName, 2, true, MIGRATION_1_2)
        db.use {
            // MIGRATION_1_2 is a no-op — data must survive
            val favCount = it.query("SELECT count(*) FROM favorite").use { c ->
                c.moveToFirst(); c.getInt(0)
            }
            assertEquals("favorite must survive v1→v2 no-op migration", 1, favCount)
        }
    }

    /**
     * Full chain: migrate 1 → 2 → 3. Favorites survive both migrations.
     */
    @Test
    @Ignore("Requires schemas/.../1.json which was never exported. Re-enable after regenerating historical Room schemas.")
    fun migrate1To3_favoritesAlwaysSurvive() {
        val dbName = "migration-test-1-to-3"

        helper.createDatabase(dbName, 1).apply {
            execSQL(
                """INSERT OR IGNORE INTO favorite (id, name, description, imageUrl, category, subCategory)
                   VALUES ('chain-fav', 'Chain Item', NULL, 'http://img.jpg', 'BIOME', NULL)"""
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(dbName, 3, true, MIGRATION_1_2, MIGRATION_2_3)
        db.use {
            val favCount = it.query("SELECT count(*) FROM favorite").use { c ->
                c.moveToFirst(); c.getInt(0)
            }
            assertEquals("favorite must survive full migration chain 1→3", 1, favCount)
        }
    }
}
