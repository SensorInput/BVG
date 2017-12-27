package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model._
import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import de.htw.ai.busbunching.model.route.LineStringRoute

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
				result += RouteCalculator.calculateDistanceBetweenPoints(coordinates(elem), coordinates(elem + 1))
			}
		}
		result
	}


	override def calculateProgressOnRoute(route: Route, geoLngLat: GeoLngLat): Double = {
		route match {
			case lineStringRoute: LineStringRoute =>
				calculateProgressOnRoute(lineStringRoute.getLineString, geoLngLat)
			case _ =>
				-1
		}
	}

	def calculateProgressOnRoute(lineString: GeoLineString, geoLngLat: GeoLngLat): Double = {
		val coordinates = asScalaBuffer(lineString.getCoordinates)
		var result: Double = 0

		import scala.util.control.Breaks._

		breakable {
			for (elem <- coordinates.indices) {
				if (elem + 1 < coordinates.size) {
					val x = coordinates(elem)
					val y = coordinates(elem + 1)

					if (RouteCalculator.isPointOnLine(x, y, geoLngLat)) {
						result += RouteCalculator.calculateDistanceBetweenPoints(x, geoLngLat)
						break()
					} else {
						result += RouteCalculator.calculateDistanceBetweenPoints(x, y)
					}
				}
			}
		}
		result
	}


	override def calculateRelativeVehiclePositions(route: Route, mainVehicle: Vehicle,
												   vehicles: util.List[Vehicle]): util.List[VehicleRelativePosition] = {
		return null;
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
			if ((prevMeasurePoint != null && RouteCalculator.calculateDistanceBetweenPoints(prevMeasurePoint.getLngLat, x.getLngLat) > 0.05)
				|| prevMeasurePoint == null) {
				var possiblePoints: List[(GeoLngLat, Double)] = Nil
				for (elem <- coordinates.indices) {
					if (elem + 1 < coordinates.size) {
						val startPoint = coordinates(elem)
						val endPoint = coordinates(elem + 1)
						val nearestPoint = RouteCalculator.getClosestPointOnSegment(startPoint, endPoint, x.getLngLat)
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
