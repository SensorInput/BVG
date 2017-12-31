package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model._
import de.htw.ai.busbunching.model.geometry.GeoLngLat

import scala.collection.JavaConverters._
import scala.collection.mutable

object RouteCalculator {

	def calculateDistanceBetweenPoints(c1: GeoLngLat, c2: GeoLngLat): Double = {
		getDistance(c1.getLat, c1.getLng, c2.getLat, c2.getLng)
	}

	// Source: https://www.htmlgoodies.com/beyond/javascript/calculate-the-distance-between-two-points-in-your-web-apps.html
	/**
	  * Calculate distance between two geo points in meters.
	  *
	  * @param lat1 latitude x
	  * @param lon1 longitude x
	  * @param lat2 latitude y
	  * @param lon2 longitude y
	  * @return distance in meters
	  */
	private def getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {
		val theta: Double = lon1 - lon2
		var dist: Double = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
		dist = Math.acos(dist)
		dist = rad2deg(dist)
		dist = dist * 60 * 1.1515
		dist * 1.609344 * 1000
	}

	private def deg2rad(deg: Double): Double = {
		deg * Math.PI / 180.0
	}

	// This function converts radians to decimal degrees
	private def rad2deg(rad: Double): Double = {
		rad * 180 / Math.PI
	}

	def isPointOnLine(x: GeoLngLat, y: GeoLngLat, point: GeoLngLat): Boolean = {
		if (y.getLng - x.getLng != 0) {
			val m = (y.getLat - x.getLat) / (y.getLng - x.getLng)
			val n = x.getLat - m * x.getLng

			return m * point.getLng + n == point.getLat
		} else if (x.getLng == point.getLng) {
			return true
		}
		false
	}

	// http://www.java2s.com/Code/Java/2D-Graphics-GUI/Returnsclosestpointonsegmenttopoint.htm
	def getClosestPointOnSegment(start: GeoLngLat, end: GeoLngLat, point: GeoLngLat): (GeoLngLat, Double) = {
		val nearestPoint = getClosestPointOnSegment(start.getLng, start.getLat, end.getLng, end.getLat, point.getLng, point.getLat)
		if (nearestPoint != null) {
			(nearestPoint, calculateDistanceBetweenPoints(point, nearestPoint))
		} else {
			null
		}
	}

	/**
	  * Returns closest point on segment to point
	  *
	  * @param sx1 segment x coord 1
	  * @param sy1 segment y coord 1
	  * @param sx2 segment x coord 2
	  * @param sy2 segment y coord 2
	  * @param px  point x coord
	  * @param py  point y coord
	  * @return closets point on segment to point
	  */
	def getClosestPointOnSegment(sx1: Double, sy1: Double, sx2: Double, sy2: Double, px: Double, py: Double): GeoLngLat = {
		val xDelta = sx2 - sx1
		val yDelta = sy2 - sy1
		if ((xDelta == 0) && (yDelta == 0)) throw new IllegalArgumentException("Segment start equals segment end")
		val u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta)

		if (u <= 1.0 && u >= 0.0) {
			new GeoLngLat(sx1 + u * xDelta.round.toInt, sy1 + u * yDelta.round.toInt)
		} else {
			null
		}
	}

	def smoothPoint(point: GeoLngLat, previousPoint: GeoLngLat = null, coordinates: mutable.Buffer[GeoLngLat]): GeoLngLat = {
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

trait RouteCalculator {

	/**
	  * Calculates the route in meters.
	  *
	  * @param route route
	  * @return distance in meters
	  */
	def calculateTotalRoute(route: Route): Double

	def calculateProgressOnRoute(route: Route, geoLngLat: GeoLngLat): Double

	def calculateRelativeVehiclePositions(route: Route, mainVehicle: Vehicle, vehicles: util.List[Vehicle], journeys: util.List[Journey]): util.List[VehicleRelativePosition] = {
		// Calculate pasted distance
		vehicles.forEach(v => v.setPastedDistance(calculateProgressOnRoute(route, v.getPosition)))
		mainVehicle.setPastedDistance(calculateProgressOnRoute(route, mainVehicle.getPosition))

		// calculate relative distance
		asScalaBuffer(vehicles).map(v => {
			val timeDistance = calculateTimeDistance(v.getPosition, mainVehicle.getPosition, journeys, route)
			val wayDistance = v.getPastedDistance - mainVehicle.getPastedDistance

			new VehicleRelativePosition(v.getRef, v.getPosition, wayDistance, timeDistance)
		}).sortWith(_.getRelativeDistance < _.getRelativeDistance).toList.asJava
	}

	def calculateTimeDistance(start: GeoLngLat, end: GeoLngLat, journeys: util.List[Journey], route: Route): Double

	def smoothVehiclePosition(position: GeoLngLat, route: Route): GeoLngLat

	def smoothJourneyCoordinates(journey: Journey, route: Route): util.List[MeasurePoint]
}
