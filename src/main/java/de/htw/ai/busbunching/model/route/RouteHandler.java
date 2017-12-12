package de.htw.ai.busbunching.model.route;

import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.model.Route;
import org.geojson.Feature;

import java.sql.Connection;

public interface RouteHandler {
	Route convertRoute(Feature feature);

	RouteStoreHandler getDatabaseHandler(Connection connection);
}
