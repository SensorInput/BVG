package de.htw.ai.busbunching.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Journey {

	private long id;
	private long routeId;
	private Timestamp startTime;
	private Timestamp endTime;

	private List<MeasurePoint> points;

	public Journey() {
	}

	public Journey(long id, long routeId, Timestamp startTime, Timestamp endTime) {
		this.id = id;
		this.routeId = routeId;
		this.startTime = startTime;
		this.endTime = endTime;
		points = new ArrayList<>();
	}

	public Journey(long id, long routeId, Timestamp startTime, Timestamp endTime, List<MeasurePoint> points) {
		this.id = id;
		this.routeId = routeId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.points = points;
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

	public List<MeasurePoint> getPoints() {
		return points;
	}

	public void setPoints(List<MeasurePoint> points) {
		this.points = points;
	}
}
