package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.Optional;

public class VehicleGetRequest implements Route {

	private Settings settings;

	public VehicleGetRequest(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		VehicleHandler handler = new VehicleHandler(connection);

		String ref = request.params("id");
		Optional<Vehicle> vehicle = handler.getVehicle(ref);

		connection.close();

		if (vehicle.isPresent()) {
			response.type("application/json; charset=utf-8");
			return vehicle.get();
		} else {
			response.status(404);
			response.type("text/html; charset=utf-8");
			return null;
		}
	}
}
