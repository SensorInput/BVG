package de.htw.ai.busbunching.model.geometry;

import java.util.LinkedList;
import java.util.List;

public class GeoLineString {

	private List<GeoLngLat> list;

	public GeoLineString() {
		list = new LinkedList<>();
	}

	public GeoLineString(List<GeoLngLat> list) {
		this.list = list;
	}

	public List<GeoLngLat> getCoordinates() {
		return list;
	}
}
