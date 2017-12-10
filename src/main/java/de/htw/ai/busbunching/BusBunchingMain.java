package de.htw.ai.busbunching;

import de.htw.ai.busbunching.handler.PositionContext;
import de.htw.ai.busbunching.handler.RouteGetContext;
import de.htw.ai.busbunching.handler.RouteImportContext;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.settings.SettingsHandler;
import de.htw.ai.busbunching.transformer.JsonTransformer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static spark.Spark.*;

public class BusBunchingMain {
	public static void main(String[] args) {
		Settings settings = null;
		try {
			Path settingsPath = Paths.get("settings.properties");
			if (Files.notExists(settingsPath)) {
				SettingsHandler.saver().createDefault(settingsPath);
			}
			settings = SettingsHandler.loader().load(settingsPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (settings == null) {
			return;
		}

		port(4545);

		post("/position", "application/json", new PositionContext(settings));
		get("/route/:route", new RouteGetContext(settings), new JsonTransformer());
		post("/route", "application/json", new RouteImportContext(settings));
	}
}
