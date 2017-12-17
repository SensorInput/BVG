package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.route.CommonRouteHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.route.RouteType;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.Optional;
import java.util.stream.Stream;

public class RouteGeoJsonGetContext implements Route {

	private Settings settings;

	public RouteGeoJsonGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		CommonRouteHandler handler = new CommonRouteHandler(connection);

		int routeId = Integer.valueOf(request.params("id"));
		response.type("application/json; charset=utf-8");

		final Optional<de.htw.ai.busbunching.model.Route> route = Stream.of(RouteType.values())
				.map(type -> RouteFactory.getHandler(type).getDatabaseHandler(connection).getRoute(routeId))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();

		if (route.isPresent()) {
			return route.get();
		}

		return null;
	}
}
