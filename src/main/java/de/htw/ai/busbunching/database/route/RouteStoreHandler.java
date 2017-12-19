package de.htw.ai.busbunching.database.route;

import de.htw.ai.busbunching.model.Route;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RouteStoreHandler {
	void save(Route route) throws SQLException;

	List<Route> getRoutes(String ref) throws SQLException;

	Optional<Route> getRoute(long id);
}
