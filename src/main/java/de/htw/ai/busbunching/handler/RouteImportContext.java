package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.database.RouteHandler;
import de.htw.ai.busbunching.model.Route;
import de.htw.ai.busbunching.model.RouteConverter;
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
		RouteHandler handler = new RouteHandler(connection);

		FeatureCollection featureCollection = new ObjectMapper().readValue(request.bodyAsBytes(), FeatureCollection.class);
		for (Feature feature : featureCollection.getFeatures()) {
			if (feature.getProperties().containsKey("type") && feature.getProperties().get("type").equals("route")) {
				Route line = RouteConverter.getLine(feature);
				if (line != null) {
					System.out.printf("Import Route: %s into database\n", line.getRef());
					handler.importLine(line);
				}
			}
		}

		connection.close();
		response.status(HttpServletResponse.SC_OK);
		return null;
	}
}
