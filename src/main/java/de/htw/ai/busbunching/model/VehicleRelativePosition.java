package de.htw.ai.busbunching.model;

import de.htw.ai.busbunching.model.geometry.GeoLngLat;

public class VehicleRelativePosition {

	private final String ref;

	private final GeoLngLat geoLngLat;
	private final double relativeDistance;
	private final double relativeTimeDistance;

	public VehicleRelativePosition(String ref, GeoLngLat geoLngLat, double relativeDistance, double relativeTimeDistance) {
		this.ref = ref;
		this.geoLngLat = geoLngLat;
		this.relativeDistance = relativeDistance;
		this.relativeTimeDistance = relativeTimeDistance;
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

	public double getRelativeTimeDistance() {
		return relativeTimeDistance;
	}
}
