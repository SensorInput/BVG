package de.htw.ai.busbunching.database;

import de.htw.ai.busbunching.model.Route;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RouteHandler {

	private Connection connection;

	public RouteHandler(Connection connection) {
		this.connection = connection;
	}

	public long importLine(Route line) throws SQLException {
		long lineId = insertLine(line);
		line.setId(lineId);
		insertMultiLineString(line);
		return lineId;
	}

	private long insertLine(Route line) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO Route VALUES (0, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, line.getOsmId());
		stmt.setString(2, line.getRef());
		stmt.setString(3, line.getName());
		stmt.setString(4, line.getType());
		stmt.setString(5, line.getNetwork());
		stmt.setString(6, line.getOperator());
		stmt.setString(7, line.getFrom());
		stmt.setString(8, line.getTo());

		int affectedRows = stmt.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				throw new SQLException("Creating line failed, no ID obtained.");
			}
		} finally {
			stmt.close();
		}
	}

	private void insertMultiLineString(Route line) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO Multiline VALUES (0, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, line.getMultiLineStringAsString());
		stmt.setLong(2, line.getId());

		stmt.executeUpdate();
		stmt.close();
	}

	public List<Route> getRoutes(String ref) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Route WHERE ref = ?");
		stmt.setString(1, ref);

		ResultSet rs = stmt.executeQuery();

		List<Route> routes = new LinkedList<>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String osmId = rs.getString("@id");
			String refFetched = rs.getString("ref");
			String name = rs.getString("name");
			String type = rs.getString("type");
			String network = rs.getString("network");
			String operator = rs.getString("operator");
			String from = rs.getString("from");
			String to = rs.getString("to");

			Route route = new Route(id, osmId, refFetched, name, type, network, operator, from, to);
			routes.add(route);
		}

		rs.close();
		stmt.close();
		return routes;
	}
}
