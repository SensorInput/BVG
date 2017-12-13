package de.htw.ai.busbunching.model;

public class MeasurePoint {
	private long time;
	private long journeyId;
	private double lat;
	private double lng;

	public MeasurePoint() {
	}

	public MeasurePoint(long time, long journeyId, double lat, double lng) {
		this.time = time;
		this.journeyId = journeyId;
		this.lat = lat;
		this.lng = lng;
	}

	public long getTime() {
		return time;
	}

	public long getJourneyId() {
		return journeyId;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
}
