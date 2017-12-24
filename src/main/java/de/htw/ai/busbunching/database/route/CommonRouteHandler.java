package de.htw.ai.busbunching.database.route;

import de.htw.ai.busbunching.database.DatabaseHandler;
import de.htw.ai.busbunching.model.Route;

import java.sql.*;

public class CommonRouteHandler extends DatabaseHandler {

	private Connection connection;

	public CommonRouteHandler(Connection connection) {
		this.connection = connection;
	}

	public long importLine(Route route) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement("INSERT INTO Route VALUES (0, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, route.getOsmId());
			stmt.setString(2, route.getRef());
			stmt.setString(3, route.getName());
			stmt.setString(4, route.getType());
			stmt.setString(5, route.getNetwork());
			stmt.setString(6, route.getOperator());
			stmt.setString(7, route.getFrom());
			stmt.setString(8, route.getTo());
			stmt.setString(9, route.getRouteType().name());

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
}
