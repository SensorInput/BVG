package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.route.CommonRouteHandler;
import de.htw.ai.busbunching.model.route.RouteFactory;
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
		CommonRouteHandler handler = new CommonRouteHandler(connection);

		String route = request.params("route");
		response.type("application/json; charset=utf-8");

		List<de.htw.ai.busbunching.model.Route> routes = new LinkedList<>();
		for (RouteType type : RouteType.values()) {
			routes.addAll(RouteFactory.getHandler(type).getDatabaseHandler(connection).getRoutes(route));
		}
		return routes;
	}
}
