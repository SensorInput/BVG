package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.JourneyHandler;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;

public class JourneyGetContext implements Route {

	private Settings settings;

	public JourneyGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		JourneyHandler handler = new JourneyHandler(connection);

		long id = Long.valueOf(request.params("id"));
		response.type("application/json; charset=utf-8");
		return handler.getJourney(id);
	}
}
