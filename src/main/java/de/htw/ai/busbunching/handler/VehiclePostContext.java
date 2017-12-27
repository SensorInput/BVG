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

public class VehiclePostContext implements Route {

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

		Vehicle vehicle = objectMapper.readValue(request.bodyAsBytes(), Vehicle.class);
		long id = handler.insert(vehicle);

		connection.close();
		return id;
	}
}
