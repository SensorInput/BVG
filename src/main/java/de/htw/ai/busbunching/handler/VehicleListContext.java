package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
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
		VehicleHandler handler = new VehicleHandler(connection);

		String ref = request.params("id");
		Optional<Vehicle> vehicleOp = handler.getVehicle(ref);

		if (vehicleOp.isPresent()) {
			Vehicle mainVehicle = vehicleOp.get();
			final Optional<de.htw.ai.busbunching.model.Route> routeOptional = RouteStoreHandler.getRoute(mainVehicle.getRouteId(), connection);
			if (routeOptional.isPresent()) {
				final de.htw.ai.busbunching.model.Route route = routeOptional.get();
				final List<Vehicle> vehicles = handler.getVehicles(mainVehicle.getRouteId());

				final RouteCalculator routeCalculator = RouteFactory.getHandler(route.getRouteType()).getRouteCalculator();
				final List<VehicleRelativePosition> positions = routeCalculator.calculateRelativeVehiclePositions(route, mainVehicle, vehicles);
				response.type("application/json; charset=utf-8");
				return positions;
			}
		}

		connection.close();

		response.status(404);
		return null;
	}
}
