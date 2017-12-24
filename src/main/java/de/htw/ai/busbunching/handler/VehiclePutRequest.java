package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;

public class VehiclePutRequest implements Route {

	private Settings settings;
	private ObjectMapper objectMapper;

	public VehiclePutRequest(Settings settings) {
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
