package de.htw.ai.busbunching.model;

import java.sql.Timestamp;

public class Journey {

	private long id;
	private long routeId;
	private Timestamp startTime;
	private Timestamp endTime;

	public Journey() {
	}

	public Journey(long id, long routeId, Timestamp startTime, Timestamp endTime) {
		this.id = id;
		this.routeId = routeId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long getId() {
		return id;
	}

	public long getRouteId() {
		return routeId;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}
}
