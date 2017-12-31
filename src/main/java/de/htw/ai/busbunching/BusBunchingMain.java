package de.htw.ai.busbunching;

import de.htw.ai.busbunching.handler.*;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.settings.SettingsHandler;
import de.htw.ai.busbunching.transformer.GeoJsonTransformer;
import de.htw.ai.busbunching.transformer.JsonTransformer;
import de.htw.ai.busbunching.utils.DatabaseUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

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

		System.out.println("Check Database");
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);

		try {
			DatabaseUtils.createTables(connection);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Start Server");
		port(4545);

		path("/api/v1", () -> {
			get("/journey/:id", new JourneyGetContext(settings), new JsonTransformer());
			post("/journey", "application/json", new JourneyPostContext(settings));
			post("/journey/position", "application/json", new JourneyPositionContext(settings));

			get("/route/:route", new RouteGetContext(settings), new JsonTransformer());
			get("/route/geo/:id", new RouteGeoJsonGetContext(settings), new GeoJsonTransformer());
			post("/route", "application/json", new RouteImportContext(settings));

			get("/vehicle/:id", new VehicleGetContext(settings), new JsonTransformer());
			get("/vehicle/:id/list", new VehicleListContext(settings), new JsonTransformer());
			post("/vehicle", "application/json", new VehiclePostContext(settings), new JsonTransformer());
			put("/vehicle/:id", "application/json", new VehiclePutContext(settings), new JsonTransformer());
		});
	}
}
