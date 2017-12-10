package de.htw.ai.busbunching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.geojson.LngLatAlt;
import org.geojson.MultiLineString;

import java.util.List;

public class Route {

	private long id;

	private String osmId;

	/**
	 * The ref describes the line number of a public transport line. (e.g. M17, 165, X11, TXL)
	 */
	private String ref;
	private String name;
	private String type;

	private String network;
	private String operator;

	private String from;
	private String to;

	private MultiLineString multiLineString;

	public Route(String osmId, String ref, String name, String type, String network, String operator, String from, String to, MultiLineString multiLineString) {
		this.osmId = osmId;
		this.ref = ref;
		this.name = name;
		this.type = type;
		this.network = network;
		this.operator = operator;
		this.from = from;
		this.to = to;
		this.multiLineString = multiLineString;
	}

	public Route(long id, String osmId, String ref, String name, String type, String network, String operator, String from, String to) {
		this.id = id;
		this.osmId = osmId;
		this.ref = ref;
		this.name = name;
		this.type = type;
		this.network = network;
		this.operator = operator;
		this.from = from;
		this.to = to;
	}

	public Route(long id, String osmId, String ref, String name, String type, String network, String operator, String from, String to, MultiLineString multiLineString) {
		this.id = id;
		this.osmId = osmId;
		this.ref = ref;
		this.name = name;
		this.type = type;
		this.network = network;
		this.operator = operator;
		this.from = from;
		this.to = to;
		this.multiLineString = multiLineString;
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

	@JsonIgnore
	public MultiLineString getMultiLineString() {
		return multiLineString;
	}

	@JsonIgnore
	public String getMultiLineStringAsString() {
		StringBuilder builder = new StringBuilder();

		builder.append("MultiLineString(");

		for (List<LngLatAlt> list : multiLineString.getCoordinates()) {
			builder.append("(");

			for (int i = 0; i < list.size(); i++) {
				LngLatAlt point = list.get(i);
				builder.append(point.getLongitude());
				builder.append(" ");
				builder.append(point.getLatitude());

				if (i + 1 < list.size()) {
					builder.append(", ");
				}
			}

			builder.append(")");
		}
		builder.append(")");

		return builder.toString();
	}

	public void setId(long id) {
		this.id = id;
	}
}