package de.htw.ai.busbunching.model;

public class MeasurePoint {
	private long time;
	private String ref;
	private double lat;
	private double lng;

	public MeasurePoint() {
	}

	public MeasurePoint(long time, String ref, double lat, double lng) {
		this.time = time;
		this.ref = ref;
		this.lat = lat;
		this.lng = lng;
	}

	public long getTime() {
		return time;
	}

	public String getRef() {
		return ref;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
}
