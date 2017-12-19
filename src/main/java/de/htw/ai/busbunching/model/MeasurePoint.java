package de.htw.ai.busbunching.model;

import de.htw.ai.busbunching.model.geometry.GeoLngLat;

public class MeasurePoint {
	private long time;
	private long journeyId;
	private GeoLngLat lngLat;

	public MeasurePoint() {
	}

	public MeasurePoint(long time, long journeyId, double lat, double lng) {
		this.time = time;
		this.journeyId = journeyId;
		this.lngLat = new GeoLngLat(lng, lat);
	}

	public MeasurePoint(long time, long journeyId, GeoLngLat lngLat) {
		this.time = time;
		this.journeyId = journeyId;
		this.lngLat = lngLat;
	}

	public long getTime() {
		return time;
	}

	public long getJourneyId() {
		return journeyId;
	}

	public GeoLngLat getLngLat() {
		return lngLat;
	}

	public void setLngLat(GeoLngLat lngLat) {
		this.lngLat = lngLat;
	}
}
