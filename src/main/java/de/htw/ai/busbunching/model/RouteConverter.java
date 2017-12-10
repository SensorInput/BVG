package de.htw.ai.busbunching.model;

import org.geojson.Feature;
import org.geojson.MultiLineString;

public class RouteConverter {

	public static Route getLine(Feature feature) {
		String osmId = String.valueOf(feature.getProperties().get("@id"));
		String ref = String.valueOf(feature.getProperties().get("ref"));
		String name = String.valueOf(feature.getProperties().get("name"));
		String network = String.valueOf(feature.getProperties().get("network"));
		String operator = String.valueOf(feature.getProperties().get("operator"));
		String form = String.valueOf(feature.getProperties().get("from"));
		String to = String.valueOf(feature.getProperties().get("to"));
		String type = String.valueOf(feature.getProperties().get("route"));

		if (feature.getGeometry() instanceof MultiLineString) {
			return new Route(osmId, ref, name, type, network, operator, form, to, (MultiLineString) feature.getGeometry());
		} else {
			System.out.println(ref + " has no multiline string");
		}
		return null;
	}
}
