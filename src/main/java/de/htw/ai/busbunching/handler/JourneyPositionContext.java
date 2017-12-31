package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.database.MeasurePointHandler;
import de.htw.ai.busbunching.model.MeasurePoint;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class JourneyPositionContext implements Route {

	private Settings settings;
	private ObjectMapper objectMapper;

	public JourneyPositionContext(Settings settings) {
		this.settings = settings;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		MeasurePointHandler measurePointHandler = new MeasurePointHandler(connection);

		try {
			MeasurePoint measurePoint = objectMapper.readValue(request.bodyAsBytes(), MeasurePoint.class);
			long id = measurePointHandler.importMeasurePoint(measurePoint);

			if (id != -1) {
				response.status(HttpServletResponse.SC_CREATED);
				return id;
			} else {
				response.status(HttpServletResponse.SC_BAD_REQUEST);
				return "Bad payload for MeasurePoint";
			}
		} catch (JsonParseException | JsonMappingException e) {
			e.printStackTrace();
			response.status(HttpServletResponse.SC_BAD_REQUEST);
			return e.getMessage();
		} finally {
			connection.close();
		}
	}

}
