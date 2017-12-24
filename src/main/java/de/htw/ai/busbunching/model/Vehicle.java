package de.htw.ai.busbunching.model;

import de.htw.ai.busbunching.model.geometry.GeoLngLat;

public class Vehicle {

	private long id;
	private String ref;

	private long routeId;
	private long time;
	private GeoLngLat position;

	public Vehicle() {
		this(null, 0);
	}

	public Vehicle(String ref, int routeId) {
		this(ref, routeId, 0, new GeoLngLat());
	}

	public Vehicle(String ref, long routeId, long time, GeoLngLat position) {
		this(0, ref, routeId, time, position);
	}

	public Vehicle(long id, String ref, long routeId, long time, GeoLngLat position) {
		this.id = id;
		this.ref = ref;
		this.routeId = routeId;
		this.time = time;
		this.position = position;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public GeoLngLat getPosition() {
		return position;
	}

	public void setPosition(GeoLngLat position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Vehicle{" +
				"id=" + id +
				", ref='" + ref + '\'' +
				", routeId=" + routeId +
				", time=" + time +
				", position=" + position +
				'}';
	}
}
