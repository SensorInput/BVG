package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Optional;

public class RouteGeoJsonGetContext implements spark.Route {

	private Settings settings;

	public RouteGeoJsonGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		final String idParam = request.params("id");

		try (Connection connection = DatabaseUtils.createDatabaseConnection(settings)) {
			long routeId = Long.valueOf(idParam);
			final Optional<Route> route = RouteStoreHandler.getRoute(routeId, connection);

			if (route.isPresent()) {
				response.type("application/json; charset=utf-8");
				return route.get();
			} else {
				response.status(HttpServletResponse.SC_NOT_FOUND);
				response.type("text/html; charset=utf-8");
				return null;
			}
		} catch (NumberFormatException e) {
			response.type("text/html; charset=utf-8");
			response.status(HttpServletResponse.SC_BAD_REQUEST);
			return String.format("'%s' is not a valid number", idParam);
		}
	}
}
