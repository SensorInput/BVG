package de.htw.ai.busbunching.factory;

import de.htw.ai.busbunching.model.route.RouteType;

public class RouteFactory {

	public static RouteHandler getHandler(RouteType routeType) {
		if (routeType == RouteType.LINE) {
			return new LineStringRouteHandler();
		} else if (routeType == RouteType.MULTILINE) {
			return new MultiLineStringRouteHandler();
		} else {
			return new MultiLineStringRouteHandler(); // Default handler
		}
	}
}
