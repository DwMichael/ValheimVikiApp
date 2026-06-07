package com.rabbitv.valheimviki.e2e.localreal

import androidx.test.platform.app.InstrumentationRegistry
import java.sql.DriverManager
import java.util.UUID

class LocalDockerPostgres {

	fun readText(textId: String): String =
		connection().use { connection ->
			connection.prepareStatement(
				"""SELECT original_text FROM "Texts" WHERE text_id = ?"""
			).use { statement ->
				statement.setObject(1, UUID.fromString(textId))
				statement.executeQuery().use { result ->
					check(result.next()) { "No Texts row found for $textId" }
					result.getString(1)
				}
			}
		}

	fun updateText(textId: String, text: String) {
		connection().use { connection ->
			connection.prepareStatement(
				"""UPDATE "Texts" SET original_text = ? WHERE text_id = ?"""
			).use { statement ->
				statement.setString(1, text)
				statement.setObject(2, UUID.fromString(textId))
				check(statement.executeUpdate() == 1) { "No Texts row updated for $textId" }
			}
		}
	}

	private fun connection() =
		DriverManager.getConnection(dbUrl(), dbUser(), dbPassword())

	private fun dbUrl(): String =
		args().getString("e2eDbUrl") ?: DEFAULT_DB_URL

	private fun dbUser(): String =
		args().getString("e2eDbUser") ?: DEFAULT_DB_USER

	private fun dbPassword(): String =
		args().getString("e2eDbPassword") ?: DEFAULT_DB_PASSWORD

	private fun args() = InstrumentationRegistry.getArguments()

	private companion object {
		init {
			Class.forName("org.postgresql.Driver")
		}

		const val DEFAULT_DB_URL = "jdbc:postgresql://10.0.2.2:5005/valheim_viki_db"
		const val DEFAULT_DB_USER = "postgres"
		const val DEFAULT_DB_PASSWORD = "valheimviki_local_password"
	}
}
