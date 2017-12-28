package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.JourneyHandler;
import de.htw.ai.busbunching.database.MeasurePointHandler;
import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Journey;
import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.model.VehicleRelativePosition;
import de.htw.ai.busbunching.route.RouteCalculator;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class VehicleListContext implements Route {

	private Settings settings;

	public VehicleListContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		VehicleHandler vehicleHandler = new VehicleHandler(connection);
		JourneyHandler journeyHandler = new JourneyHandler(connection);
		MeasurePointHandler measurePointHandler = new MeasurePointHandler(connection);

		String ref = request.params("id");
		Optional<Vehicle> vehicleOp = vehicleHandler.getVehicle(ref);

		if (vehicleOp.isPresent()) {
			Vehicle mainVehicle = vehicleOp.get();

			final List<Journey> journeys = journeyHandler.getJourneysByRouteId(mainVehicle.getRouteId());
			final Optional<de.htw.ai.busbunching.model.Route> routeOptional = RouteStoreHandler.getRoute(mainVehicle.getRouteId(), connection);

			if (routeOptional.isPresent()) {
				final de.htw.ai.busbunching.model.Route route = routeOptional.get();
				final List<Vehicle> vehicles = vehicleHandler.getVehicles(mainVehicle.getRouteId());
				final RouteCalculator routeCalculator = RouteFactory.getHandler(route.getRouteType()).getRouteCalculator();

				// Fetch MeasurePoints and smooth points
				for (Journey journey : journeys) {
					journey.setPoints(measurePointHandler.getMeasurePoints(journey.getId())); // Fetch from db
					journey.setPoints(routeCalculator.smoothJourneyCoordinates(journey, route)); // Smooth
				}

				final List<VehicleRelativePosition> positions = routeCalculator.calculateRelativeVehiclePositions(route, mainVehicle, vehicles, journeys);
				response.type("application/json; charset=utf-8");
				return positions;
			}
		}

		connection.close();

		response.status(404);
		return null;
	}
}
