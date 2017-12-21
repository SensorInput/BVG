package de.htw.ai.busbunching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.htw.ai.busbunching.model.geometry.GeoLngLat;

public class MeasurePoint {
	private long id;
	@JsonIgnore
	private long journeyId;
	private long time;
	private GeoLngLat lngLat;

	public MeasurePoint() {
	}

	public MeasurePoint(long id, long journeyId, long time, double lat, double lng) {
		this(id, journeyId, time, new GeoLngLat(lng, lat));
	}

	public MeasurePoint(long id, long journeyId, long time, GeoLngLat lngLat) {
		this.id = id;
		this.journeyId = journeyId;
		this.time = time;
		this.lngLat = lngLat;
	}

	public long getTime() {
		return time;
	}

	public long getJourneyId() {
		return journeyId;
	}

	public long getId() {
		return id;
	}

	public GeoLngLat getLngLat() {
		return lngLat;
	}

	public void setLngLat(GeoLngLat lngLat) {
		this.lngLat = lngLat;
	}
}
