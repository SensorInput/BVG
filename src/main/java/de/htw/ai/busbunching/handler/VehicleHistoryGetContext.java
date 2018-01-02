package de.htw.ai.busbunching.handler;

import de.htw.ai.busbunching.database.VehicleHandler;
import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VehicleHistoryGetContext implements spark.Route {

	private Settings settings;

	public VehicleHistoryGetContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		VehicleHandler handler = new VehicleHandler(connection);

		final List<Vehicle> vehiclesHistory;
		String ref = request.params(":ref");
		if (ref != null) {
			vehiclesHistory = handler.getVehiclesHistory(ref);
		} else {
			vehiclesHistory = handler.getVehiclesHistory();
		}
		connection.close();

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		List<String> vehicles = new ArrayList<>();
		for (Vehicle vehicle : vehiclesHistory) {
			vehicles.add(vehicle.getRef() + " @ " + vehicle.getPosition().getLat() + " " + vehicle.getPosition().getLng() + " @ " + dateFormat.format(vehicle.getTime()));
		}
		return vehicles;
	}
}
