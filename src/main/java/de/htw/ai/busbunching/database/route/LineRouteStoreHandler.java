package de.htw.ai.busbunching.database.route;

import de.htw.ai.busbunching.database.DatabaseHandler;
import de.htw.ai.busbunching.geojson.GeoJsonLineStringConverter;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoLineString;
import de.htw.ai.busbunching.model.route.LineStringRoute;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class LineRouteStoreHandler extends DatabaseHandler implements RouteStoreHandler {

	private Connection connection;

	public LineRouteStoreHandler(Connection connection) {
		this.connection = connection;
	}

	private void insertLineString(LineStringRoute line) {
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("INSERT INTO Line VALUES (0, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, GeoJsonLineStringConverter.lineStringToString(line.getLineString()));
			stmt.setLong(2, line.getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, null);
		}
	}

	@Override
	public void save(Route route) {
		CommonRouteHandler commonRouteHandler = new CommonRouteHandler(connection);
		long id = commonRouteHandler.importLine(route);
		route.setId(id);
		insertLineString((LineStringRoute) route);
	}

	@Override
	public List<Route> getRoutes(String ref) {
		List<Route> routes = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement("SELECT * FROM Route WHERE ref = ? AND geometry = \"LINE\"");
			stmt.setString(1, ref);

			rs = stmt.executeQuery();

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

				LineStringRoute route = new LineStringRoute(id, osmId, refFetched, name, type, network, operator, from, to);

				getLineString(id).ifPresent(data -> {
					GeoLineString geoLineString = GeoJsonLineStringConverter.stringToLineString(data);
					route.setGeoLineString(geoLineString);
				});

				routes.add(route);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return routes;
	}

	private Optional<String> getLineString(long id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Line WHERE ref = ?");
			stmt.setLong(1, id);

			rs = stmt.executeQuery();

			if (rs.first()) {
				return Optional.of(rs.getString("line"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Route> getRoute(long id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Route WHERE id = ? AND geometry = \"LINE\"");
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

				final LineStringRoute route = new LineStringRoute(id, osmId, refFetched, name, type, network, operator, from, to);

				getLineString(id).ifPresent(data -> {
					GeoLineString geoLineString = GeoJsonLineStringConverter.stringToLineString(data);
					route.setGeoLineString(geoLineString);
				});

				return Optional.of(route);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(stmt, rs);
		}
		return Optional.empty();
	}
}
