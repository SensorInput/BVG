package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat, GeoMultiLineString}
import de.htw.ai.busbunching.model.route.MultiLineStringRoute
import de.htw.ai.busbunching.model.{Journey, MeasurePoint, Route}

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

	private def getRelevantLineString(multiLineString: GeoMultiLineString): GeoLineString = {
		val lines = asScalaBuffer(multiLineString.getLines)
		val lineStringRouteCalculator = new LineStringRouteCalculator
		lines.maxBy(lineStringRouteCalculator.calculateTotalRoute)
	}

	override def smoothJourneyCoordinates(journey: Journey, route: Route): Unit = {
		route match {
			case multiLine: MultiLineStringRoute =>
				val lineString = getRelevantLineString(multiLine.getMultiLineString)
				val coordinates = asScalaBuffer(lineString.getCoordinates)

				val resultList = new util.ArrayList[GeoLngLat]()
				var prevMeasurePoint: MeasurePoint = null
				journey.getPoints.forEach(x => {
					if ((prevMeasurePoint != null && calculateDistanceBetweenPoints(prevMeasurePoint.getLngLat, x.getLngLat) > 0.05) || prevMeasurePoint == null) {
						var possiblePoints: List[(GeoLngLat, Double)] = Nil
						for (elem <- coordinates.indices) {
							if (elem + 1 < coordinates.size) {
								val startPoint = coordinates(elem)
								val endPoint = coordinates(elem + 1)
								val nearestPoint = getClosestPointOnSegment(startPoint, endPoint, x.getLngLat)
								if (nearestPoint != null) {
									possiblePoints = nearestPoint :: possiblePoints
								}
							}
						}
						if (possiblePoints != Nil) {
							val nearestPoint = possiblePoints.minBy(x => x._2)._1
							print(" [ " + nearestPoint.getLng + ", " + nearestPoint.getLat + " ], ")
						}
					}
					prevMeasurePoint = x
				})
			case _ =>
		}
	}
}
