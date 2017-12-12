package de.htw.ai.busbunching.model.route;

import de.htw.ai.busbunching.database.route.LineRouteStoreHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.geojson.GeoJsonLineStringConverter;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoLineString;
import org.geojson.Feature;
import org.geojson.LineString;

import java.sql.Connection;

public class LineStringRouteHandler implements RouteHandler {

	@Override
	public Route convertRoute(Feature feature) {
		String osmId = String.valueOf(feature.getProperties().get("@id"));
		String ref = String.valueOf(feature.getProperties().get("ref"));
		String name = String.valueOf(feature.getProperties().get("name"));
		String network = String.valueOf(feature.getProperties().get("network"));
		String operator = String.valueOf(feature.getProperties().get("operator"));
		String form = String.valueOf(feature.getProperties().get("from"));
		String to = String.valueOf(feature.getProperties().get("to"));
		String type = String.valueOf(feature.getProperties().get("route"));

		if (feature.getGeometry() instanceof LineString) {
			final LineString geometry = (LineString) feature.getGeometry();
			final GeoLineString lineString = GeoJsonLineStringConverter.convertGeoJsonToLineString(geometry);
			return new LineStringRoute(osmId, ref, name, type, network, operator, form, to, lineString);
		} else {
			return null;
		}
	}

	@Override
	public RouteStoreHandler getDatabaseHandler(Connection connection) {
		return new LineRouteStoreHandler(connection);
	}
}
