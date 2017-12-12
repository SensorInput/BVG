package de.htw.ai.busbunching.geojson

import de.htw.ai.busbunching.model.geometry.GeoMultiLineString
import org.geojson.MultiLineString

import scala.collection.JavaConverters._

object GeoJsonMultilineStringConverter {

	def convertGeoJsonToMultiLineString(multiLineString: MultiLineString): GeoMultiLineString = {
		val lines = asScalaBuffer(multiLineString.getCoordinates)
		new GeoMultiLineString(lines.map(GeoJsonLineStringConverter.convertGeoJsonToLineString).asJava)
	}

	def multiLineStringToString(multiLineString: GeoMultiLineString): String = {
		val lines = asScalaBuffer(multiLineString.getLines)
		lines.map(GeoJsonLineStringConverter.lineStringToString).mkString("MultiLineString(", ", ", ")")
	}
}
