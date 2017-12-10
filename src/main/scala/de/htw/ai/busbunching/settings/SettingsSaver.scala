package de.htw.ai.busbunching.settings

import java.io.IOException
import java.nio.file.Path

/**
  * Created by tobias on 05.02.17.
  */
trait SettingsSaver {

	@throws[IOException]
	def save(settings: Settings, path: Path)

	def createDefault(path: Path): Unit = save(new Settings(), path)
}
