package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.factory.RouteFactory;
import de.htw.ai.busbunching.factory.RouteHandler;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.route.RouteType;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class RouteImportContext implements spark.Route {

	private Settings settings;

	public RouteImportContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);

		FeatureCollection featureCollection = new ObjectMapper().readValue(request.bodyAsBytes(), FeatureCollection.class);
		for (Feature feature : featureCollection.getFeatures()) {
			if (feature.getProperties().containsKey("type") && feature.getProperties().get("type").equals("route")) {
				final RouteType routeType = RouteType.getRouteType(feature);

				final RouteHandler handler = RouteFactory.getHandler(routeType);
				Route line = handler.convertRoute(feature);
				if (line != null) {
					System.out.printf("Import Route: %s into database\n", line.getRef());
					handler.getDatabaseHandler(connection).save(line);
				}
			}
		}

		connection.close();
		response.status(HttpServletResponse.SC_OK);
		return null;
	}
}
