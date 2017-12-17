package de.htw.ai.busbunching.factory;

import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.route.RouteCalculator;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;

import java.sql.Connection;

public interface RouteHandler {
	Route convertRoute(Feature feature);

	RouteStoreHandler getDatabaseHandler(Connection connection);

	GeoJsonObject convertToGeoJson(Route route);

	RouteCalculator getRouteCalculator();
}
