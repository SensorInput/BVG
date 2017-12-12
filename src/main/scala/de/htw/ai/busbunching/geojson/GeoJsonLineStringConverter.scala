package de.htw.ai.busbunching.geojson

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import org.geojson.{LineString, LngLatAlt}

import scala.collection.JavaConverters._

object GeoJsonLineStringConverter {

	def convertGeoJsonToLineString(line: util.List[LngLatAlt]): GeoLineString = {
		val list = asScalaBuffer(line).map(coordinate => new GeoLngLat(coordinate.getLongitude, coordinate.getLongitude))
		new GeoLineString(list.asJava)
	}

	def convertGeoJsonToLineString(line: LineString): GeoLineString = {
		convertGeoJsonToLineString(line.getCoordinates)
	}

	def lineStringToString(line: GeoLineString): String = {
		val coordinates = asScalaBuffer(line.getCoordinates)
		coordinates.map(point => point.toString).mkString("(", ", ", ")")
	}
}
