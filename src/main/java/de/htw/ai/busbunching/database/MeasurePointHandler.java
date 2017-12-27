package de.htw.ai.busbunching.database;

import de.htw.ai.busbunching.model.MeasurePoint;
import de.htw.ai.busbunching.model.geometry.GeoLngLat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeasurePointHandler extends DatabaseHandler {

	private Connection connection;

	public MeasurePointHandler(Connection connection) {
		this.connection = connection;
	}

	public long importMeasurePoint(MeasurePoint point) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("INSERT INTO MeasurePoint VALUES (0, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, point.getJourneyId());
			stmt.setLong(2, point.getTime());
			stmt.setDouble(3, point.getLngLat().getLat());
			stmt.setDouble(4, point.getLngLat().getLng());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				throw new SQLException("Creating MeasurePoint failed, no ID obtained.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return -1;
	}

	public List<MeasurePoint> getMeasurePoints(long requestId) {
		List<MeasurePoint> points = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement("SELECT * FROM MeasurePoint WHERE journey_id = ?");
			stmt.setLong(1, requestId);

			rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				int journeyId = rs.getInt("journey_id");
				double lng = rs.getDouble("lng");
				double lat = rs.getDouble("lat");
				long time = rs.getLong("time");
				points.add(new MeasurePoint(id, journeyId, time, new GeoLngLat(lng, lat)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return points;
	}
}
