package de.htw.ai.busbunching.database.route;

import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.route.RouteType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RouteStoreHandler {
	void save(Route route) throws SQLException;

	List<Route> getRoutes(String ref) throws SQLException;

	Optional<Route> getRoute(long id);

	static Optional<Route> getRoute(long id, Connection connection) {
		return Stream.of(RouteType.values())
				.map(type -> RouteFactory.getHandler(type).getDatabaseHandler(connection).getRoute(id))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();
	}
}
