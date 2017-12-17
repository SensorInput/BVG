package de.htw.ai.busbunching.route

import de.htw.ai.busbunching.model.Route
import de.htw.ai.busbunching.model.geometry.GeoLngLat

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
}
