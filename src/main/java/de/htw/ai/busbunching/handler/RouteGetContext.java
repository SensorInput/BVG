package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.RouteHandler;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;

public class RouteGetContext implements Route {

	private Settings settings;

	public RouteGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		RouteHandler handler = new RouteHandler(connection);

		String route = request.params("route");
		response.type("application/json; charset=utf-8");
		return handler.getRoutes(route);
	}
}
