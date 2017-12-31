package de.htw.ai.busbunching.model;

import de.htw.ai.busbunching.model.route.RouteType;

public abstract class Route {

	private long id;

	private final String osmId;

	/**
	 * The ref describes the line number of a public transport line. (e.g. M17, 165, X11, TXL)
	 */
	private final String ref;
	private final String name;
	private final String type;

	private final String network;
	private final String operator;

	private final String from;
	private final String to;

	private final RouteType routeType;

	public Route(String osmId, String ref, String name, String type, String network,
				 String operator, String from, String to, RouteType routeType) {
		this(-1, osmId, ref, name, type, network, operator, from, to, routeType);
	}

	public Route(long id, String osmId, String ref, String name, String type, String network,
				 String operator, String from, String to, RouteType routeType) {
		this.id = id;
		this.osmId = osmId;
		this.ref = ref;
		this.name = name;
		this.type = type;
		this.network = network;
		this.operator = operator;
		this.from = from;
		this.to = to;
		this.routeType = routeType;
	}

	public long getId() {
		return id;
	}

	public String getOsmId() {
		return osmId;
	}

	public String getRef() {
		return ref;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getNetwork() {
		return network;
	}

	public String getOperator() {
		return operator;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RouteType getRouteType() {
		return routeType;
	}
}