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

public class VehiclePostContext implements spark.Route {

	private Settings settings;
	private ObjectMapper objectMapper;

	public VehiclePostContext(Settings settings) {
		this.settings = settings;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		VehicleHandler handler = new VehicleHandler(connection);

		try {
			Vehicle vehicle = objectMapper.readValue(request.bodyAsBytes(), Vehicle.class);

			if (vehicle.getPosition() != null) {
				final Optional<Route> route = RouteStoreHandler.getRoute(vehicle.getRouteId(), connection);
				route.ifPresent(val -> {
					final RouteCalculator routeCalculator = RouteFactory.getHandler(val.getRouteType()).getRouteCalculator();
					vehicle.setPosition(routeCalculator.smoothPosition(vehicle.getPosition(), val));
				});
			}

			long id = handler.insert(vehicle);
			if (id != -1) {
				return id;
			} else {
				response.status(HttpServletResponse.SC_BAD_REQUEST);
				return "Bad payload for vehicle";
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
