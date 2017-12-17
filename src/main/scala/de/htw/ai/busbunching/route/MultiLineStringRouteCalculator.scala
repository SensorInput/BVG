package de.htw.ai.busbunching.route

import de.htw.ai.busbunching.model.Route
import de.htw.ai.busbunching.model.route.MultiLineStringRoute

import scala.collection.JavaConverters._


class MultiLineStringRouteCalculator extends RouteCalculator {
	/**
	  * Calculates the route in meters.
	  *
	  * @param route route
	  * @return distance in meters
	  */
	override def calculateTotalRoute(route: Route): Double = {
		route match {
			case multiLine: MultiLineStringRoute =>
				val lines = asScalaBuffer(multiLine.getMultiLineString.getLines)
				val lineStringRouteCalculator = new LineStringRouteCalculator
				lines.map(lineStringRouteCalculator.calculateTotalRoute).max
			case _ =>
				-1
		}
	}
}
