package de.htw.ai.busbunching.model.route;

import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoLineString;

public class LineStringRoute extends Route {

	private static final RouteType TYPE = RouteType.LINE;

	private GeoLineString geoLineString;

	public LineStringRoute(String osmId, String ref, String name, String type, String network, String operator, String from, String to) {
		super(osmId, ref, name, type, network, operator, from, to, TYPE);
	}

	public LineStringRoute(long id, String osmId, String ref, String name, String type, String network, String operator, String from, String to) {
		super(id, osmId, ref, name, type, network, operator, from, to, TYPE);
	}

	public LineStringRoute(String osmId, String ref, String name, String type, String network, String operator, String from, String to, GeoLineString geoLineString) {
		super(osmId, ref, name, type, network, operator, from, to, TYPE);
		this.geoLineString = geoLineString;
	}

	public GeoLineString getLineString() {
		return geoLineString;
	}
}
