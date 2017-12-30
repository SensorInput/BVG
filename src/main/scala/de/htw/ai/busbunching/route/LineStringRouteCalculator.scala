package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model._
import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import de.htw.ai.busbunching.model.route.LineStringRoute

import scala.collection.JavaConverters._
import scala.collection.mutable


class LineStringRouteCalculator extends RouteCalculator {

	implicit class ImplDoubleVecUtils(values: Seq[Double]) {
		def mean: Double = values.sum / values.length
	}

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

	override def calculateTimeDistance(start: GeoLngLat, end: GeoLngLat, journeys: util.List[Journey], route: Route): Double = {
		route match {
			case lineStringRoute: LineStringRoute =>
				calculateTimeDistance(start, end, journeys, lineStringRoute.getLineString)
			case _ =>
				-1
		}
	}

	def calculateTimeDistance(start: GeoLngLat, end: GeoLngLat, journeys: util.List[Journey], lineString: GeoLineString): Double = {
		asScalaBuffer(journeys)
			.filter(journey => !journey.getPoints.isEmpty)
			.map(journey => calculateTimeDistance(start, end, journey, lineString))
			.mean
	}

	private def calculateTimeDistance(start: GeoLngLat, end: GeoLngLat, journey: Journey, lineString: GeoLineString): Double = {
		if (start == end) {
			0
		} else {
			val startPoint = findClosestPointOnJourney(start, journey, lineString)
			val endPoint = findClosestPointOnJourney(end, journey, lineString)

			Math.abs(startPoint.getTime - endPoint.getTime)
		}
	}

	private def findClosestPointOnJourney(point: GeoLngLat, journey: Journey, lineString: GeoLineString): MeasurePoint = {
		val distancePoint = calculateProgressOnRoute(lineString, point)
		val measurePoints = asScalaBuffer(journey.getPoints)

		val closestPoint = measurePoints.map(point => (calculateProgressOnRoute(lineString, point.getLngLat), point))
			.minBy(p => Math.abs(p._1 - distancePoint))
		closestPoint._2
	}

	override def smoothVehiclePosition(position: GeoLngLat, route: Route): GeoLngLat = {
		route match {
			case lineStringRoute: LineStringRoute =>
				val coordinates = asScalaBuffer(lineStringRoute.getLineString.getCoordinates)
				smoothVehiclePosition(position, coordinates)
			case _ =>
				null
		}
	}

	def smoothVehiclePosition(position: GeoLngLat, coordinates: mutable.Buffer[GeoLngLat]): GeoLngLat = {
		smoothPoint(point = position, coordinates = coordinates)
	}

	override def smoothJourneyCoordinates(journey: Journey, route: Route): util.List[MeasurePoint] = {
		route match {
			case lineStringRoute: LineStringRoute =>
				val coordinates = asScalaBuffer(lineStringRoute.getLineString.getCoordinates)
				smoothJourneyCoordinates(journey, coordinates)
			case _ =>
				null
		}
	}

	def smoothJourneyCoordinates(journey: Journey, coordinates: mutable.Buffer[GeoLngLat]): util.List[MeasurePoint] = {
		val resultList = new util.ArrayList[MeasurePoint]()

		var prevMeasurePoint: GeoLngLat = null

		journey.getPoints.forEach(x => {
			val result = smoothPoint(x.getLngLat, prevMeasurePoint, coordinates)
			prevMeasurePoint = x.getLngLat
			if (result != null) {
				resultList.add(new MeasurePoint(x.getId, x.getJourneyId, x.getTime, result))
			}
		})
		resultList
	}

	private def smoothPoint(point: GeoLngLat, previousPoint: GeoLngLat = null, coordinates: mutable.Buffer[GeoLngLat]): GeoLngLat = {
		if ((previousPoint != null && RouteCalculator.calculateDistanceBetweenPoints(previousPoint, point) > 50) || previousPoint == null) {
			var possiblePoints: List[(GeoLngLat, Double)] = Nil
			for (elem <- coordinates.indices) {
				if (elem + 1 < coordinates.size) {
					val startPoint = coordinates(elem)
					val endPoint = coordinates(elem + 1)
					val nearestPoint = RouteCalculator.getClosestPointOnSegment(startPoint, endPoint, point)
					if (nearestPoint != null) {
						possiblePoints = nearestPoint :: possiblePoints
					}
				}
			}
			// Get closest segment to point
			if (possiblePoints != Nil) {
				val lngLat = possiblePoints.minBy(x => x._2)._1
				return lngLat
			}
		}
		null
	}
}
