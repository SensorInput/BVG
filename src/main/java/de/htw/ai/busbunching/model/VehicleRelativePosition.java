package de.htw.ai.busbunching.model;

import de.htw.ai.busbunching.model.geometry.GeoLngLat;

public class VehicleRelativePosition {

	private final String ref;

	private final GeoLngLat geoLngLat;
	private final double relativeDistance;

	public VehicleRelativePosition(String ref, GeoLngLat geoLngLat, double relativeDistance) {
		this.ref = ref;
		this.geoLngLat = geoLngLat;
		this.relativeDistance = relativeDistance;
	}

	public String getRef() {
		return ref;
	}

	public GeoLngLat getGeoLngLat() {
		return geoLngLat;
	}

	public double getRelativeDistance() {
		return relativeDistance;
	}
}
