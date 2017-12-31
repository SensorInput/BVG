package de.htw.ai.busbunching.factory;

import de.htw.ai.busbunching.model.route.RouteType;

public class RouteFactory {

	public static RouteHandler getHandler(RouteType routeType) {
		switch (routeType) {
			case LINE:
				return new LineStringRouteHandler();
			case MULTILINE:
				return new MultiLineStringRouteHandler();
			default:
				return new MultiLineStringRouteHandler(); // Default handler

		}
	}
}
