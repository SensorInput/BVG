package de.htw.ai.busbunching.route

import java.util

import de.htw.ai.busbunching.model.geometry.{GeoLineString, GeoLngLat}
import org.junit.{Assert, Test}

class RouteCalculatorTest {

	@Test
	def testIsOnPointWithNormalValuesShouldReturnTrue(): Unit = {
		val result = RouteCalculator.isPointOnLine(new GeoLngLat(1, 1), new GeoLngLat(3, 3), new GeoLngLat(2, 2))
		Assert.assertTrue("Point not on route", result)
	}

	@Test
	def testIsOnPointWithXShouldReturnTrue(): Unit = {
		val result = RouteCalculator.isPointOnLine(new GeoLngLat(1, 1), new GeoLngLat(3, 1), new GeoLngLat(2, 1))
		Assert.assertTrue("Point not on route", result)
	}

	@Test
	def testIsOnPointWithYShouldReturnTrue(): Unit = {
		val result = RouteCalculator.isPointOnLine(new GeoLngLat(1, 1), new GeoLngLat(1, 3), new GeoLngLat(1, 2))
		Assert.assertTrue("Point not on route", result)
	}

	@Test
	def testIsOnPointShouldReturnFalse(): Unit = {
		val result = RouteCalculator.isPointOnLine(new GeoLngLat(1, 1), new GeoLngLat(3, 3), new GeoLngLat(2, 1))
		Assert.assertFalse("Point not on route", result)
	}
}
