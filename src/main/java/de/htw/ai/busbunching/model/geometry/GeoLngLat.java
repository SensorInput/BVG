package de.htw.ai.busbunching.model.geometry;

import java.util.Objects;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GeoLngLat geoLngLat = (GeoLngLat) o;
		return Double.compare(geoLngLat.lng, lng) == 0 &&
				Double.compare(geoLngLat.lat, lat) == 0;
	}

	@Override
	public int hashCode() {

		return Objects.hash(lng, lat);
	}

	@Override
	public String toString() {
		return lng + " " + lat;
	}
}
