package de.htw.ai.busbunching.settings

import java.io.IOException
import java.lang.reflect.Modifier
import java.nio.file.{Files, Path}
import java.util.Properties

/**
  * Created by tobias on 05.02.17.
  */
class PropertiesSettingsHandler extends SettingsLoader with SettingsSaver {

	@throws[IOException]
	override def load(path: Path): Settings = {
		val properties = new Properties()
		properties.load(Files.newBufferedReader(path))

		val settings = new Settings()
		classOf[Settings].getDeclaredFields
			.filter(f => !Modifier.isTransient(f.getModifiers))
			.filter(f => properties.containsKey(f.getName))
			.foreach(f => {
				f.setAccessible(true)
				if (f.getType == Integer.TYPE) {
					f.setInt(settings, properties.getProperty(f.getName).toInt)
				} else {
					f.set(settings, properties.getProperty(f.getName))
				}
			})
		settings
	}

	@throws[IOException]
	override def save(settings: Settings, path: Path): Unit = {
		val properties = new Properties()

		classOf[Settings].getDeclaredFields
			.filter(f => !Modifier.isTransient(f.getModifiers))
			.foreach(f => {
				f.setAccessible(true)
				properties.setProperty(f.getName, f.get(settings).toString)
			})

		properties.store(Files.newOutputStream(path), "Settings")
	}
}
