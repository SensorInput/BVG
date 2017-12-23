package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.factory.RouteHandler;
import de.htw.ai.busbunching.model.route.RouteType;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class RouteGetContext implements Route {

	private Settings settings;

	public RouteGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);

		String route = request.params("route");

		List<de.htw.ai.busbunching.model.Route> routes = new LinkedList<>();
		for (RouteType type : RouteType.values()) {
			RouteHandler handler = RouteFactory.getHandler(type);
			if (handler != null) {
				RouteStoreHandler databaseHandler = handler.getDatabaseHandler(connection);
				routes.addAll(databaseHandler.getRoutes(route));
			}
		}
//
//		for (de.htw.ai.busbunching.model.Route r : routes) {
//			System.out.println(RouteFactory.getHandler(r.getRouteType()).getRouteCalculator().calculateTotalRoute(r));
//		}

		connection.close();

		if (routes.isEmpty()) {
			response.status(404);
			response.type("text/html; charset=utf-8");
			return null; // Return 404 for empty route list
		}
		response.type("application/json; charset=utf-8");
		return routes;
	}
}
