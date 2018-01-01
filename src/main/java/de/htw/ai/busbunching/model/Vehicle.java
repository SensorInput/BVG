package de.htw.ai.busbunching.model;

import de.htw.ai.busbunching.model.geometry.GeoLngLat;

public class Vehicle {

	private Long id;
	private String ref;

	private Long routeId;
	private Long time;
	private GeoLngLat position;

	private double pastedDistance;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public GeoLngLat getPosition() {
		return position;
	}

	public void setPosition(GeoLngLat position) {
		this.position = position;
	}

	public double getPastedDistance() {
		return pastedDistance;
	}

	public void setPastedDistance(double pastedDistance) {
		this.pastedDistance = pastedDistance;
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
