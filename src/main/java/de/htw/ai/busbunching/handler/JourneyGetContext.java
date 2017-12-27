package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.JourneyHandler;
import de.htw.ai.busbunching.database.MeasurePointHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Journey;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.Optional;

public class JourneyGetContext implements Route {

	private Settings settings;

	public JourneyGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		JourneyHandler handler = new JourneyHandler(connection);
		MeasurePointHandler measurePointHandler = new MeasurePointHandler(connection);

		long id = Long.valueOf(request.params("id"));

		Journey journey = handler.getJourney(id);
		if (journey != null) {
			journey.setPoints(measurePointHandler.getMeasurePoints(journey.getId()));

			final Optional<de.htw.ai.busbunching.model.Route> route = RouteStoreHandler.getRoute(journey.getRouteId(), connection);

			route.ifPresent(val -> journey.setPoints(RouteFactory.getHandler(val.getRouteType())
					.getRouteCalculator().smoothJourneyCoordinates(journey, val)));
		}
		connection.close();

		if (journey == null) {
			response.type("text/html; charset=utf-8");
			response.status(404);
			return null;
		} else {
			response.type("application/json; charset=utf-8");
			return journey;
		}
	}
}
