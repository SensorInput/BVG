package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoMultiLineString}
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

	override def smoothJourneyCoordinates(journey: Journey, route: Route): util.List[MeasurePoint] = {
		route match {
			case multiLine: MultiLineStringRoute =>
				val lineString = getRelevantLineString(multiLine.getMultiLineString)
				val lineStringRouteCalculator = new LineStringRouteCalculator
				lineStringRouteCalculator.smoothCoordinates(journey, asScalaBuffer(lineString.getCoordinates))
			case _ =>
				null
		}
	}
}
