package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.route.RouteCalculator;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Optional;

public class VehiclePutContext implements spark.Route {

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

		try {
			Vehicle vehicle = objectMapper.readValue(request.bodyAsBytes(), Vehicle.class);
			vehicle.setRef(ref);
			vehicle.setTime(System.currentTimeMillis());

			long routeId = handler.getVehicle(vehicle.getRef()).orElse(vehicle).getRouteId();
			if (routeId == 0) {
				routeId = vehicle.getRouteId();
			}

			if (vehicle.getPosition() != null) {
				final Optional<Route> route = RouteStoreHandler.getRoute(routeId, connection);
				route.ifPresent(val -> {
					final RouteCalculator routeCalculator = RouteFactory.getHandler(val.getRouteType()).getRouteCalculator();
					vehicle.setPosition(routeCalculator.smoothPosition(vehicle.getPosition(), val));
				});

				// Save old vehicle position into history database
				handler.getVehicle(ref).ifPresent(handler::insertIntoHistory);
			}

			boolean success = handler.update(vehicle);
			if (success) {
				response.status(HttpServletResponse.SC_NO_CONTENT);
				return 0;
			} else {
				response.status(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
		} catch (JsonMappingException | JsonParseException e) {
			e.printStackTrace();
			response.status(HttpServletResponse.SC_BAD_REQUEST);
			return e.getMessage();
		} finally {
			connection.close();
		}
	}
}
