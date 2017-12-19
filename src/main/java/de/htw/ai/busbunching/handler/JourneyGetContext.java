package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.JourneyHandler;
import de.htw.ai.busbunching.database.MeasurePointHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Journey;
import de.htw.ai.busbunching.model.route.RouteType;
import de.htw.ai.busbunching.route.MultiLineStringRouteCalculator;
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

		MeasurePointHandler measurePointHandler = new MeasurePointHandler(connection);

		long id = Long.valueOf(request.params("id"));
		response.type("application/json; charset=utf-8");


		Journey journey = handler.getJourney(id);
		journey.setPoints(measurePointHandler.getMeasurePoints(id));


		de.htw.ai.busbunching.model.Route route = RouteFactory.getHandler(RouteType.MULTILINE).getDatabaseHandler(connection).getRoute(journey.getRouteId()).get();
		new MultiLineStringRouteCalculator().smoothJourneyCoordinates(journey, route);

		connection.close();
		return journey;
	}
}
