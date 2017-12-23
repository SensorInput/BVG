package de.htw.ai.busbunching.handler;

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

		int routeId = Integer.valueOf(request.params("id"));

		final Optional<de.htw.ai.busbunching.model.Route> route = Stream.of(RouteType.values())
				.map(type -> RouteFactory.getHandler(type).getDatabaseHandler(connection).getRoute(routeId))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();

		if (route.isPresent()) {
			response.type("application/json; charset=utf-8");
			return route.get();
		} else {
			response.type("text/html; charset=utf-8");
			response.status(404);
			return null;
		}
	}
}
