package de.htw.ai.busbunching.database.route;

import de.htw.ai.busbunching.model.Route;

import java.sql.SQLException;
import java.util.List;

public interface RouteStoreHandler {
	void save(Route route) throws SQLException;

	List<Route> getRoutes(String ref) throws SQLException;
}
