package de.htw.ai.busbunching.model.route;

import de.htw.ai.busbunching.database.route.MultiLineRouteStoreHandler;
import de.htw.ai.busbunching.database.route.RouteStoreHandler;
import de.htw.ai.busbunching.geojson.GeoJsonMultilineStringConverter;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoMultiLineString;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.MultiLineString;

import java.sql.Connection;

public class MultiLineStringRouteHandler implements RouteHandler {

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

		if (feature.getGeometry() instanceof MultiLineString) {
			final MultiLineString geometry = (MultiLineString) feature.getGeometry();
			final GeoMultiLineString multiLineString = GeoJsonMultilineStringConverter.convertGeoJsonToMultiLineString(geometry);
			return new MultiLineStringRoute(osmId, ref, name, type, network, operator, form, to, multiLineString);
		} else {
			return null;
		}
	}

	@Override
	public RouteStoreHandler getDatabaseHandler(Connection connection) {
		return new MultiLineRouteStoreHandler(connection);
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
		if (route instanceof MultiLineStringRoute) {
			final MultiLineString geometry = GeoJsonMultilineStringConverter.convertMultiLineStringToGeoJson(((MultiLineStringRoute) route).getMultiLineString());
			value.setGeometry(geometry);
		}
		return value;
	}
}
