package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import de.htw.ai.busbunching.model.route.LineStringRoute
import de.htw.ai.busbunching.model.{Journey, Route}

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

	override def smoothJourneyCoordinates(journey: Journey, route: Route): Unit = {
		route match {
			case lineStringRoute: LineStringRoute =>
				val coordinates = asScalaBuffer(lineStringRoute.getLineString.getCoordinates)

				val resultList = new util.ArrayList[GeoLngLat]()
				journey.getPoints.forEach(x => {
					var possiblePoints: List[(GeoLngLat, Double)] = Nil
					for (elem <- coordinates.indices) {
						if (elem + 1 < coordinates.size) {
							val nearestPoint = getClosestPointOnSegment(coordinates(elem), coordinates(elem + 1), x.getLngLat)
							if (nearestPoint != null) {
								possiblePoints = nearestPoint :: possiblePoints
							}
						}
					}
					val nearestPoint = possiblePoints.maxBy(x => x._2)._1
					print(" [ " + nearestPoint.getLng + ", " + nearestPoint.getLat + " ], ")
				})
			case _ =>
		}
	}
}
