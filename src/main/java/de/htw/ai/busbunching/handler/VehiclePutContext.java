package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.route.RouteCalculator;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.Optional;

public class VehiclePutContext implements Route {

	private Settings settings;
	private ObjectMapper objectMapper;

	public VehiclePutContext(Settings settings) {
		this.settings = settings;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		VehicleHandler handler = new VehicleHandler(connection);


		String ref = request.params("id");
		Vehicle vehicle = objectMapper.readValue(request.bodyAsBytes(), Vehicle.class);
		vehicle.setRef(ref);
		boolean success = handler.update(vehicle);

		final Optional<de.htw.ai.busbunching.model.Route> route = RouteStoreHandler.getRoute(vehicle.getRouteId(), connection);
		route.ifPresent(val -> {
			final RouteCalculator routeCalculator = RouteFactory.getHandler(val.getRouteType()).getRouteCalculator();
			double distance = routeCalculator.calculateProgressOnRoute(val, vehicle.getPosition());
			vehicle.setPastedDistance(distance);
		});

		connection.close();
		if (success) {
			response.status(204);
			return 0;
		} else {
			response.status(404);
			return null;
		}
	}
}
