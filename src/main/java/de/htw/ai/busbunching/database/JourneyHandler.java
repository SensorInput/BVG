package de.htw.ai.busbunching.database;

import de.htw.ai.busbunching.model.Journey;

import java.sql.*;

public class JourneyHandler {

	private Connection connection;

	public JourneyHandler(Connection connection) {
		this.connection = connection;
	}

	public long insertJourney(Journey journey) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO Journey VALUES (0, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, journey.getRouteId());
		stmt.setTimestamp(2, journey.getStartTime());
		stmt.setTimestamp(3, journey.getEndTime());

		int affectedRows = stmt.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				throw new SQLException("Creating Journey failed, no ID obtained.");
			}
		} finally {
			stmt.close();
		}
	}

	public Journey getJourney(long id) {
		try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Journey WHERE id = ?")) {
			stmt.setLong(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.first()) {
					long routeId = rs.getLong("route_id");
					Timestamp startTime = rs.getTimestamp("startTime");
					Timestamp endTime = rs.getTimestamp("endTime");

					return new Journey(id, routeId, startTime, endTime);
				}
				return null;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
