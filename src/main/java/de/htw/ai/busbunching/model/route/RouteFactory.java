package de.htw.ai.busbunching.model.route;

public class RouteFactory {

	public static RouteHandler getHandler(RouteType routeType) {
		if (routeType == RouteType.LINE) {
			return new LineStringRouteHandler();
		} else if (routeType == RouteType.MULTILINE) {
			return new MultiLineStringRouteHandler();
		} else {
			return null;
		}
	}
}
