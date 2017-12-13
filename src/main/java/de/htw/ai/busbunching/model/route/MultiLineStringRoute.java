package de.htw.ai.busbunching.model.route;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.geometry.GeoMultiLineString;

public class MultiLineStringRoute extends Route {

	private static final RouteType TYPE = RouteType.MULTILINE;

	@JsonIgnore
	private GeoMultiLineString multiLineString;

	public MultiLineStringRoute(String osmId, String ref, String name, String type, String network, String operator, String from, String to) {
		super(osmId, ref, name, type, network, operator, from, to, TYPE);
	}

	public MultiLineStringRoute(long id, String osmId, String ref, String name, String type, String network, String operator, String from, String to) {
		super(id, osmId, ref, name, type, network, operator, from, to, TYPE);
	}


	public MultiLineStringRoute(String osmId, String ref, String name, String type, String network, String operator, String from, String to, GeoMultiLineString multiLineString) {
		super(osmId, ref, name, type, network, operator, from, to, TYPE);
		this.multiLineString = multiLineString;
	}

	public GeoMultiLineString getMultiLineString() {
		return multiLineString;
	}

	public void setMultiLineString(GeoMultiLineString multiLineString) {
		this.multiLineString = multiLineString;
	}
}
