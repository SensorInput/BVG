package de.htw.ai.busbunching.geojson

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import org.geojson.{LineString, LngLatAlt}

import scala.collection.JavaConverters._

object GeoJsonLineStringConverter {

	def convertGeoJsonToLineString(line: util.List[LngLatAlt]): GeoLineString = {
		val list = asScalaBuffer(line).map(coordinate => new GeoLngLat(coordinate.getLongitude, coordinate.getLatitude))
		new GeoLineString(list.asJava)
	}

	def convertGeoJsonToLineString(line: LineString): GeoLineString = {
		convertGeoJsonToLineString(line.getCoordinates)
	}

	def convertLineStringToGeoJson(value: GeoLineString): LineString = {
		val coords = asScalaBuffer(value.getCoordinates)
		val array = coords.map(point => new LngLatAlt(point.getLng, point.getLat))
		new LineString(array.toArray: _*)
	}

	def convertLineStringToGeoJsonList(value: GeoLineString): util.List[LngLatAlt] = {
		asScalaBuffer(value.getCoordinates)
			.map(point => new LngLatAlt(point.getLng, point.getLat))
			.asJava
	}

	def lineStringToString(line: GeoLineString): String = {
		val coordinates = asScalaBuffer(line.getCoordinates)
		coordinates.map(point => point.toString).mkString("(", ", ", ")")
	}

	def stringToLineString(string: String): GeoLineString = {
		var value = string
		if (value.startsWith("(")) {
			value = value.substring(1)
		}
		if (value.endsWith(")")) {
			value = value.substring(0, value.length - 1)
		}
		val list = value.split(", ").map(elem => {
			val coordinates = elem.split(" ")
			new GeoLngLat(coordinates(0).toDouble, coordinates(1).toDouble)
		}).toList

		new GeoLineString(list.asJava)
	}
}
