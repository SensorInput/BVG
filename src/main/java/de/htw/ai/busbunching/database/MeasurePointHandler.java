package de.htw.ai.busbunching.database;

import de.htw.ai.busbunching.model.MeasurePoint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeasurePointHandler {

	private Connection connection;

	public MeasurePointHandler(Connection connection) {
		this.connection = connection;
	}

	public long importMeasurePoint(MeasurePoint point) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO MeasurePoint VALUES (0, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, point.getJourneyId());
		stmt.setLong(2, point.getTime());
		stmt.setDouble(3, point.getLngLat().getLat());
		stmt.setDouble(4, point.getLngLat().getLng());

		int affectedRows = stmt.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				throw new SQLException("Creating MeasurePoint failed, no ID obtained.");
			}
		} finally {
			stmt.close();
		}
	}

	public List<MeasurePoint> getMeasurePoints(long journeyId) throws SQLException {
		List<MeasurePoint> points = new ArrayList<>();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM MeasurePoint WHERE journey_id = ?");
		stmt.setLong(1, journeyId);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			double lat = rs.getDouble("lat");
			double lng = rs.getDouble("lng");
			long time = rs.getLong("time");
			points.add(new MeasurePoint(time, id, lat, lng));
		}

		rs.close();
		stmt.close();
		return points;
	}
}
