package de.htw.ai.busbunching.database.route;

import de.htw.ai.busbunching.geojson.GeoJsonMultilineStringConverter;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoMultiLineString;
import de.htw.ai.busbunching.model.route.MultiLineStringRoute;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MultiLineRouteStoreHandler implements RouteStoreHandler {

	private Connection connection;

	public MultiLineRouteStoreHandler(Connection connection) {
		this.connection = connection;
	}

	private void insertMultiLineString(MultiLineStringRoute line) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO Multiline VALUES (0, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, GeoJsonMultilineStringConverter.multiLineStringToString(line.getMultiLineString()));
		stmt.setLong(2, line.getId());

		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	public void save(Route route) throws SQLException {
		CommonRouteHandler commonRouteHandler = new CommonRouteHandler(connection);
		long id = commonRouteHandler.importLine(route);
		route.setId(id);
		insertMultiLineString((MultiLineStringRoute) route);
	}

	@Override
	public List<Route> getRoutes(String ref) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Route WHERE ref = ? AND geometry = \"MULTILINE\"");
		stmt.setString(1, ref);

		ResultSet rs = stmt.executeQuery();

		List<Route> routes = new LinkedList<>();
		while (rs.next()) {
			long id = rs.getLong("id");
			String osmId = rs.getString("@id");
			String refFetched = rs.getString("ref");
			String name = rs.getString("name");
			String type = rs.getString("type");
			String network = rs.getString("network");
			String operator = rs.getString("operator");
			String from = rs.getString("from");
			String to = rs.getString("to");


			MultiLineStringRoute route = new MultiLineStringRoute(id, osmId, refFetched, name, type, network, operator, from, to);

			getMultiLineString(id).ifPresent(data -> {
				GeoMultiLineString geoMultiLineString = GeoJsonMultilineStringConverter.stringToMultiLineString(data);
				route.setMultiLineString(geoMultiLineString);
			});
			routes.add(route);
		}

		rs.close();
		stmt.close();
		return routes;
	}

	private Optional<String> getMultiLineString(long id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Multiline WHERE ref = ?");
			stmt.setLong(1, id);

			rs = stmt.executeQuery();

			if (rs.first()) {
				return Optional.of(rs.getString("multiline"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<Route> getRoute(int id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Route WHERE id = ? AND geometry = \"MULTILINE\"");
			stmt.setLong(1, id);

			rs = stmt.executeQuery();

			if (rs.first()) {
				String osmId = rs.getString("@id");
				String refFetched = rs.getString("ref");
				String name = rs.getString("name");
				String type = rs.getString("type");
				String network = rs.getString("network");
				String operator = rs.getString("operator");
				String from = rs.getString("from");
				String to = rs.getString("to");

				MultiLineStringRoute route = new MultiLineStringRoute(id, osmId, refFetched, name, type, network, operator, from, to);

				getMultiLineString(id).ifPresent(data -> {
					GeoMultiLineString geoMultiLineString = GeoJsonMultilineStringConverter.stringToMultiLineString(data);
					route.setMultiLineString(geoMultiLineString);
				});

				return Optional.of(route);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}
}
