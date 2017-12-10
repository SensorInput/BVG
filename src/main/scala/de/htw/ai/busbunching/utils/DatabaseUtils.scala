package de.htw.ai.busbunching.utils

import java.sql.{Connection, DriverManager}

import de.htw.ai.busbunching.settings.Settings

object DatabaseUtils {

	def createDatabaseConnection(settings: Settings): Connection = {
		val databaseUrl = s"jdbc:mysql://${settings.db_host}:${settings.db_port}/${settings.db_database}?" +
			s"autoReconnect=true&wait_timeout=86400&serverTimezone=Europe/Berlin"
		DriverManager.getConnection(databaseUrl, settings.db_username, settings.db_password)
	}
}
