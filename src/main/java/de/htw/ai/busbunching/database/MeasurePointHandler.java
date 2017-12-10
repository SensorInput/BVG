package de.htw.ai.busbunching.database;

import de.htw.ai.busbunching.model.MeasurePoint;

import java.sql.*;

public class MeasurePointHandler {

	private Connection connection;

	public MeasurePointHandler(Connection connection) {
		this.connection = connection;
	}

	public long importMeasurePoint(MeasurePoint point) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO MeasurePoint VALUES (0, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, point.getRef());
		stmt.setLong(2, point.getTime());
		stmt.setDouble(3, point.getLat());
		stmt.setDouble(4, point.getLng());

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
}
