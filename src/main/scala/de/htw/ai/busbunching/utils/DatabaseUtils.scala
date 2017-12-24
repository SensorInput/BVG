package de.htw.ai.busbunching.utils

import java.sql.{Connection, DriverManager}

import de.htw.ai.busbunching.settings.Settings

object DatabaseUtils {

	def createDatabaseConnection(settings: Settings): Connection = {
		val databaseUrl = s"jdbc:mysql://${settings.db_host}:${settings.db_port}/${settings.db_database}?" +
			s"autoReconnect=true&wait_timeout=86400&serverTimezone=Europe/Berlin"
		DriverManager.getConnection(databaseUrl, settings.db_username, settings.db_password)
	}

	def createTables(connection: Connection): Unit = {
		def createTable(sql: String) = {
			val preparedStatement = connection.prepareStatement(sql)
			preparedStatement.execute()
			preparedStatement.close()
		}

		createTable(
			"""CREATE TABLE IF NOT EXISTS `Route` (
			  |  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  |  `@id` varchar(100) DEFAULT NULL,
			  |  `ref` varchar(50) DEFAULT NULL,
			  |  `name` text,
			  |  `type` varchar(100) DEFAULT NULL,
			  |  `network` varchar(200) DEFAULT '',
			  |  `operator` varchar(200) DEFAULT '',
			  |  `from` text,
			  |  `to` text,
			  |  `geometry` varchar(20) DEFAULT NULL,
			  |  PRIMARY KEY (`id`)
			  |) ENGINE=InnoDB AUTO_INCREMENT=486 DEFAULT CHARSET=latin1;""".stripMargin)

		createTable(
			"""CREATE TABLE IF NOT EXISTS `Line` (
			  |  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  |  `line` text,
			  |  `ref` int(11) unsigned DEFAULT NULL COMMENT 'Referenz zu Route',
			  |  PRIMARY KEY (`id`),
			  |  KEY `ref` (`ref`),
			  |  CONSTRAINT `Line_ibfk_1` FOREIGN KEY (`ref`) REFERENCES `Route` (`id`)
			  |) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;""".stripMargin)

		createTable(
			"""CREATE TABLE IF NOT EXISTS `Multiline` (
			  |  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  |  `multiline` text,
			  |  `ref` int(11) unsigned DEFAULT NULL COMMENT 'Referenz zu Route',
			  |  PRIMARY KEY (`id`),
			  |  KEY `ref` (`ref`),
			  |  CONSTRAINT `Multiline_ibfk_1` FOREIGN KEY (`ref`) REFERENCES `Route` (`id`)
			  |) ENGINE=InnoDB AUTO_INCREMENT=485 DEFAULT CHARSET=latin1;""".stripMargin)

		createTable(
			"""CREATE TABLE IF NOT EXISTS `Vehicle` (
			  |  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  |  `ref` varchar(16) DEFAULT NULL COMMENT 'Android Device ID',
			  |  `route_id` int(10) unsigned DEFAULT NULL,
			  |  `time` bigint(20) DEFAULT NULL,
			  |  `lng` double DEFAULT NULL,
			  |  `lat` double DEFAULT NULL,
			  |  PRIMARY KEY (`id`),
			  |  KEY `route_id` (`route_id`),
			  |  CONSTRAINT `Vehicle_ibfk_1` FOREIGN KEY (`route_id`) REFERENCES `Route` (`id`)
			  |) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;""".stripMargin)

		createTable(
			"""CREATE TABLE IF NOT EXISTS `Journey` (
			  |  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  |  `route_id` int(11) unsigned DEFAULT NULL,
			  |  `startTime` datetime DEFAULT NULL,
			  |  `endTime` datetime DEFAULT NULL,
			  |  PRIMARY KEY (`id`),
			  |  KEY `route_id` (`route_id`),
			  |  CONSTRAINT `Journey_ibfk_1` FOREIGN KEY (`route_id`) REFERENCES `Route` (`id`)
			  |) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;""".stripMargin)

		createTable(
			"""CREATE TABLE IF NOT EXISTS `MeasurePoint` (
			  |  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
			  |  `journey_id` int(11) unsigned DEFAULT NULL,
			  |  `time` bigint(20) DEFAULT NULL,
			  |  `lat` double DEFAULT NULL,
			  |  `lng` double DEFAULT NULL,
			  |  PRIMARY KEY (`id`),
			  |  KEY `journey_id` (`journey_id`),
			  |  CONSTRAINT `MeasurePoint_ibfk_1` FOREIGN KEY (`journey_id`) REFERENCES `Journey` (`id`)
			  |) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=latin1;""".stripMargin)
	}
}
