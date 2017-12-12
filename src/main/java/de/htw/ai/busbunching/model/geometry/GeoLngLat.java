package de.htw.ai.busbunching.model.geometry;

public class GeoLngLat {

	private double lng;
	private double lat;

	public GeoLngLat() {
	}

	public GeoLngLat(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public double getLat() {
		return lat;
	}

	@Override
	public String toString() {
		return lng + " " + lat;
	}
}
