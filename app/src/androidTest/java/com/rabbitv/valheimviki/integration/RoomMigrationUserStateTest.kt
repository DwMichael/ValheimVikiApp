package com.rabbitv.valheimviki.integration

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase.Companion.MIGRATION_1_2
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase.Companion.MIGRATION_2_3
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomMigrationUserStateTest {

	@get:Rule
	val helper = MigrationTestHelper(
		InstrumentationRegistry.getInstrumentation(),
		ValheimVikiDatabase::class.java
	)

	@Test
	fun current_schema_preserves_favorite_user_state_when_opened_with_migrations() = runBlocking {
		val dbName = "room-user-state-current"
		val context = InstrumentationRegistry.getInstrumentation().targetContext
		context.deleteDatabase(dbName)
		helper.createDatabase(dbName, CURRENT_DB_VERSION).apply {
			execSQL(
				"""
				INSERT INTO favorite (id, name, description, imageUrl, category, subCategory)
				VALUES ('fav-meadows', 'Meadows', 'desc', 'https://example.com/meadows.webp', 'BIOME', NULL)
				""".trimIndent()
			)
			close()
		}

		val db = Room.databaseBuilder(context, ValheimVikiDatabase::class.java, dbName)
			.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
			.build()

		try {
			val favorites = db.favoriteDao().getAllFavorites().first()
			assertEquals(1, favorites.size)
			assertEquals("fav-meadows", favorites.first().id)
		} finally {
			db.close()
		}
	}

	private companion object {
		const val CURRENT_DB_VERSION = 3
	}
}
