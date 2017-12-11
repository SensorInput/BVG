package de.htw.ai.busbunching.model;

public class MeasurePoint {
	private long time;
	private long journyId;
	private double lat;
	private double lng;

	public MeasurePoint() {
	}

	public MeasurePoint(long time, long journyId, double lat, double lng) {
		this.time = time;
		this.journyId = journyId;
		this.lat = lat;
		this.lng = lng;
	}

	public long getTime() {
		return time;
	}

	public long getJournyId() {
		return journyId;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
}
