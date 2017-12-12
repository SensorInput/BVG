package de.htw.ai.busbunching.model.route;

import org.geojson.Feature;
import org.geojson.LineString;
import org.geojson.MultiLineString;

public enum RouteType {
	LINE, MULTILINE;

	public static RouteType getRouteType(Feature feature) {
		if (feature.getGeometry() instanceof MultiLineString) {
			return MULTILINE;
		} else if (feature.getGeometry() instanceof LineString) {
			return LINE;
		}
		return null;
	}
}
