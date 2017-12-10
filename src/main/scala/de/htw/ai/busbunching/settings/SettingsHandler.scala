package de.htw.ai.busbunching.settings

/**
  * Created by tobias on 05.02.17.
  */
object SettingsHandler {

	def loader = new PropertiesSettingsHandler()

	def saver = new PropertiesSettingsHandler()

}
