package de.htw.ai.busbunching.route

import de.htw.ai.busbunching.model.Route
import de.htw.ai.busbunching.model.geometry.GeoLineString
import de.htw.ai.busbunching.model.route.LineStringRoute

import scala.collection.JavaConverters._


class LineStringRouteCalculator extends RouteCalculator {
	/**
	  * Calculates the route in meters.
	  *
	  * @param route route
	  * @return distance in meters
	  */
	override def calculateTotalRoute(route: Route): Double = {
		route match {
			case lineStringRoute: LineStringRoute =>
				calculateTotalRoute(lineStringRoute.getLineString)
			case _ =>
				-1
		}
	}

	def calculateTotalRoute(lineString: GeoLineString): Double = {
		val coordinates = asScalaBuffer(lineString.getCoordinates)
		var result: Double = 0
		for (elem <- coordinates.indices) {
			if (elem + 1 < coordinates.size) {
				result += calculateDistanceBetweenPoints(coordinates(elem), coordinates(elem + 1))
			}
		}
		result
	}
}
