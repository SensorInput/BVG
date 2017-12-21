package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import de.htw.ai.busbunching.model.route.LineStringRoute
import de.htw.ai.busbunching.model.{Journey, MeasurePoint, Route}

import scala.collection.JavaConverters._
import scala.collection.mutable


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

	override def smoothJourneyCoordinates(journey: Journey, route: Route): util.List[MeasurePoint] = {
		route match {
			case lineStringRoute: LineStringRoute =>
				val coordinates = asScalaBuffer(lineStringRoute.getLineString.getCoordinates)
				smoothCoordinates(journey, coordinates)
			case _ =>
				null
		}
	}

	def smoothCoordinates(journey: Journey, coordinates: mutable.Buffer[GeoLngLat]): util.List[MeasurePoint] = {
		val resultList = new util.ArrayList[MeasurePoint]()
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
					val lngLat = possiblePoints.minBy(x => x._2)._1
					resultList.add(new MeasurePoint(x.getId, x.getJourneyId, x.getTime, lngLat))
				}
			}
			prevMeasurePoint = x
		})
		resultList
	}
}
