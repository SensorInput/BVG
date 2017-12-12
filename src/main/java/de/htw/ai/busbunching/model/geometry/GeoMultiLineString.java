package de.htw.ai.busbunching.model.geometry;

import java.util.LinkedList;
import java.util.List;

public class GeoMultiLineString {

	private List<GeoLineString> list;

	public GeoMultiLineString() {
		list = new LinkedList<>();
	}

	public GeoMultiLineString(List<GeoLineString> list) {
		this.list = list;
	}

	public List<GeoLineString> getLines() {
		return list;
	}
}
