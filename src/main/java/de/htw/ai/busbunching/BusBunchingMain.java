package de.htw.ai.busbunching;

import de.htw.ai.busbunching.handler.*;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.settings.SettingsHandler;
import de.htw.ai.busbunching.transformer.GeoJsonTransformer;
import de.htw.ai.busbunching.transformer.JsonTransformer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static spark.Spark.*;

public class BusBunchingMain {

	private static Settings settings = null;

	public static void main(String[] args) {
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

		path("/api/v1", () -> {
			get("/journey/:id", new JourneyGetContext(settings), new JsonTransformer());
			post("/journey", "application/json", new JourneyPostContext(settings));
			post("/journey/position", "application/json", new JurneyPositionContext(settings));
			get("/route/:route", new RouteGetContext(settings), new JsonTransformer());
			get("/route/geo/:id", new RouteGeoJsonGetContext(settings), new GeoJsonTransformer());
			post("/route", "application/json", new RouteImportContext(settings));
		});
	}
}
