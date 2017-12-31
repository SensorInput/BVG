package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.JourneyHandler;
import de.htw.ai.busbunching.database.MeasurePointHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Journey;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Optional;

public class JourneyGetContext implements spark.Route {

	private Settings settings;

	public JourneyGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		JourneyHandler handler = new JourneyHandler(connection);
		MeasurePointHandler measurePointHandler = new MeasurePointHandler(connection);

		final String idParam = request.params("id");
		try {
			long id = Long.valueOf(idParam);

			Journey journey = handler.getJourney(id);
			if (journey != null) {
				journey.setPoints(measurePointHandler.getMeasurePoints(journey.getId()));

				final Optional<Route> route = RouteStoreHandler.getRoute(journey.getRouteId(), connection);
				route.ifPresent(val -> journey.setPoints(RouteFactory.getHandler(val.getRouteType())
						.getRouteCalculator().smoothJourneyCoordinates(journey, val)));
			}
			connection.close();

			if (journey == null) {
				response.type("text/html; charset=utf-8");
				response.status(HttpServletResponse.SC_NOT_FOUND);
				return null;
			} else {
				response.type("application/json; charset=utf-8");
				return journey;
			}
		} catch (NumberFormatException e) {
			response.type("text/html; charset=utf-8");
			response.status(HttpServletResponse.SC_BAD_REQUEST);
			return String.format("'%s' is not a valid number", idParam);
		}
	}
}
