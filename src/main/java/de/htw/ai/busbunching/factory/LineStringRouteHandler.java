package de.htw.ai.busbunching.factory;

import de.htw.ai.busbunching.database.route.LineRouteStoreHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.geojson.GeoJsonLineStringConverter;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoLineString;
import de.htw.ai.busbunching.model.route.LineStringRoute;
import de.htw.ai.busbunching.route.LineStringRouteCalculator;
import de.htw.ai.busbunching.route.RouteCalculator;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
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

	@Override
	public RouteCalculator getRouteCalculator() {
		return new LineStringRouteCalculator();
	}

	@Override
	public GeoJsonObject convertToGeoJson(Route route) {
		Feature value = new Feature();
		value.getProperties().put("@id", route.getOsmId());
		value.getProperties().put("ref", route.getRef());
		value.getProperties().put("name", route.getName());
		value.getProperties().put("type", route.getType());
		value.getProperties().put("network", route.getNetwork());
		value.getProperties().put("operator", route.getOperator());
		value.getProperties().put("from", route.getFrom());
		value.getProperties().put("to", route.getTo());
		if (route instanceof LineStringRoute) {
			final LineString geometry = GeoJsonLineStringConverter.convertLineStringToGeoJson(((LineStringRoute) route).getLineString());
			value.setGeometry(geometry);
		}
		return value;
	}
}
