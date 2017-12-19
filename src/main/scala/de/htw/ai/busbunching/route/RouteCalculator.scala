package de.htw.ai.busbunching.route

import de.htw.ai.busbunching.model.geometry.GeoLngLat
import de.htw.ai.busbunching.model.{Journey, Route}

trait RouteCalculator {

	/**
	  * Calculates the route in meters.
	  *
	  * @param route route
	  * @return distance in meters
	  */
	def calculateTotalRoute(route: Route): Double

	def calculateDistanceBetweenPoints(c1: GeoLngLat, c2: GeoLngLat): Double = {
		getDistance(c1.getLat, c1.getLng, c2.getLat, c2.getLng)
	}

	// Source: https://www.htmlgoodies.com/beyond/javascript/calculate-the-distance-between-two-points-in-your-web-apps.html
	private def getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {
		val theta: Double = lon1 - lon2
		var dist: Double = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
		dist = Math.acos(dist)
		dist = rad2deg(dist)
		dist = dist * 60 * 1.1515
		dist * 1.609344
	}

	private def deg2rad(deg: Double): Double = {
		deg * Math.PI / 180.0
	}

	// This function converts radians to decimal degrees
	private def rad2deg(rad: Double): Double = {
		rad * 180 / Math.PI
	}

	def smoothJourneyCoordinates(journey: Journey, route: Route)

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
	  * @param sx1
	  * segment x coord 1
	  * @param sy1
	  * segment y coord 1
	  * @param sx2
	  * segment x coord 2
	  * @param sy2
	  * segment y coord 2
	  * @param px
	  * point x coord
	  * @param py
	  * point y coord
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
}
