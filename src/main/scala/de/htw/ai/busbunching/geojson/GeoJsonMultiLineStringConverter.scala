package de.htw.ai.busbunching.geojson

import de.htw.ai.busbunching.model.geometry.GeoMultiLineString
import org.geojson.MultiLineString

import scala.collection.JavaConverters._

object GeoJsonMultiLineStringConverter {

	def convertGeoJsonToMultiLineString(multiLineString: MultiLineString): GeoMultiLineString = {
		val lines = asScalaBuffer(multiLineString.getCoordinates)
		new GeoMultiLineString(lines.map(GeoJsonLineStringConverter.convertGeoJsonToLineString).asJava)
	}

	def convertMultiLineStringToGeoJson(value: GeoMultiLineString): MultiLineString = {
		val multiLineString = new MultiLineString()
		val lines = asScalaBuffer(value.getLines)
		lines.map(line => GeoJsonLineStringConverter.convertLineStringToGeoJsonList(line))
			.foreach(multiLineString.add)
		multiLineString
	}

	def multiLineStringToString(multiLineString: GeoMultiLineString): String = {
		val lines = asScalaBuffer(multiLineString.getLines)
		lines.map(GeoJsonLineStringConverter.lineStringToString)
			.mkString("MultiLineString(", ", ", ")")
	}

	def stringToMultiLineString(string: String): GeoMultiLineString = {
		val value = string.substring(17, string.length - 2)
		val list = value.split("\\), \\(")
			.map(elem => GeoJsonLineStringConverter.stringToLineString(elem))
			.toList

		new GeoMultiLineString(list.asJava)
	}
}
