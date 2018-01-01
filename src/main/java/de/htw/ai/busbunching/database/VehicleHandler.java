package de.htw.ai.busbunching.database;

import de.htw.ai.busbunching.model.Vehicle;
import de.htw.ai.busbunching.model.geometry.GeoLngLat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleHandler extends DatabaseHandler {

	private Connection connection;

	public VehicleHandler(Connection connection) {
		this.connection = connection;
	}

	public Optional<Vehicle> getVehicle(String ref) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Vehicle WHERE ref = ?");
			stmt.setString(1, ref);

			rs = stmt.executeQuery();
			if (rs.first()) {
				long id = rs.getLong("id");
				String refFetched = rs.getString("ref");

				long routeId = rs.getLong("route_id");
				long time = rs.getLong("time");
				double lng = rs.getDouble("lng");
				double lat = rs.getDouble("lat");

				return Optional.of(new Vehicle(id, refFetched, routeId, time, new GeoLngLat(lng, lat)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return Optional.empty();
	}

	public List<Vehicle> getVehicles(long routeId) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Vehicle> vehicles = new ArrayList<>();
		try {
			stmt = connection.prepareStatement("SELECT * FROM Vehicle WHERE route_id = ?");
			stmt.setLong(1, routeId);

			rs = stmt.executeQuery();
			while (rs.next()) {
				long id = rs.getLong("id");
				String ref = rs.getString("ref");

				long time = rs.getLong("time");
				double lng = rs.getDouble("lng");
				double lat = rs.getDouble("lat");

				vehicles.add(new Vehicle(id, ref, routeId, time, new GeoLngLat(lng, lat)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return vehicles;
	}

	public long insert(Vehicle vehicle) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement("INSERT INTO Vehicle VALUES (0, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, vehicle.getRef());
			stmt.setLong(2, vehicle.getRouteId());
			stmt.setLong(3, vehicle.getTime());
			stmt.setDouble(4, vehicle.getPosition().getLng());
			stmt.setDouble(5, vehicle.getPosition().getLat());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				throw new SQLException("Creating route failed, no ID obtained.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return -1;
	}

	public boolean update(Vehicle vehicle) {
		Optional<Vehicle> oldVehicleOp = getVehicle(vehicle.getRef());

		if (!oldVehicleOp.isPresent()) {
			return false;
		}

		Vehicle oldVehicle = oldVehicleOp.get();

		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("UPDATE Vehicle SET route_id = ?, time = ?, lng = ?, lat = ? WHERE ref = ?");
			stmt.setLong(1, vehicle.getRouteId() != 0 ? vehicle.getRouteId() : oldVehicle.getRouteId());
			stmt.setLong(2, vehicle.getTime() != 0 ? vehicle.getTime() : oldVehicle.getTime());
			stmt.setDouble(3, vehicle.getPosition() != null ? vehicle.getPosition().getLng() : oldVehicle.getPosition().getLng());
			stmt.setDouble(4, vehicle.getPosition() != null ? vehicle.getPosition().getLat() : oldVehicle.getPosition().getLat());

			stmt.setString(5, vehicle.getRef());

			int affectedRows = stmt.executeUpdate();

			return affectedRows != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, null);
		}
		return false;
	}

	public void insertIntoHistory(Vehicle vehicle) {
		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement("INSERT INTO VehicleHistory VALUES (0, ?, ?, ?, ?, ?)");
			stmt.setString(1, vehicle.getRef());
			stmt.setLong(2, vehicle.getRouteId());
			stmt.setLong(3, vehicle.getTime());
			stmt.setDouble(4, vehicle.getPosition().getLng());
			stmt.setDouble(5, vehicle.getPosition().getLat());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, null);
		}
	}
}
