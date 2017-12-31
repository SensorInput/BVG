package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

	private static final String PROPERTY_TYPE = "type";
	private static final String ROUTE = "route";

	private Settings settings;

	public RouteImportContext(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		try (Connection connection = DatabaseUtils.createDatabaseConnection(settings)) {
			FeatureCollection featureCollection = new ObjectMapper().readValue(request.bodyAsBytes(), FeatureCollection.class);

			for (Feature feature : featureCollection.getFeatures()) {
				if (feature.getProperties().containsKey(PROPERTY_TYPE) && feature.getProperties().get(PROPERTY_TYPE).equals(ROUTE)) {
					final RouteType routeType = RouteType.getRouteType(feature);

					if (routeType != null) {
						final RouteHandler handler = RouteFactory.getHandler(routeType);
						final Route line = handler.convertRoute(feature);
						if (line != null) {
							System.out.printf("Import Route: %s into database\n", line.getRef());
							handler.getDatabaseHandler(connection).save(line);
						}
					}
				}
			}
			response.status(HttpServletResponse.SC_CREATED);
		} catch (JsonMappingException | JsonParseException e) {
			e.printStackTrace();
			response.status(HttpServletResponse.SC_BAD_REQUEST);
			return e.getMessage();
		}
		return null;
	}
}
