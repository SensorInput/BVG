package de.htw.ai.busbunching.transformer

import com.fasterxml.jackson.databind.ObjectMapper
import de.htw.ai.busbunching.factory.RouteFactory
import de.htw.ai.busbunching.model.Route
import spark.ResponseTransformer

class GeoJsonTransformer extends ResponseTransformer {

	private val objectMapper = new ObjectMapper()

	override def render(o: scala.Any): String = {
		o match {
			case route: Route =>
				val geoJson = RouteFactory.getHandler(route.getRouteType).convertToGeoJson(route)
				objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(geoJson)
			case _ =>
				"Invalid Data"
		}
	}
}
