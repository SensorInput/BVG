package de.htw.ai.busbunching.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.ai.busbunching.database.JourneyHandler;
import de.htw.ai.busbunching.model.Journey;
import de.htw.ai.busbunching.settings.Settings;
import de.htw.ai.busbunching.utils.DatabaseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class JourneyPostContext implements Route {

	private Settings settings;
	private ObjectMapper objectMapper;

	public JourneyPostContext(Settings settings) {
		this.settings = settings;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		try (Connection connection = DatabaseUtils.createDatabaseConnection(settings)) {
			JourneyHandler journeyHandler = new JourneyHandler(connection);

			Journey journey = objectMapper.readValue(request.bodyAsBytes(), Journey.class);
			final long id = journeyHandler.insertJourney(journey);
			if (id != -1) {
				response.status(HttpServletResponse.SC_CREATED);
				return id;
			} else {
				response.status(HttpServletResponse.SC_BAD_REQUEST);
				return "Bad payload for journey";
			}
		} catch (JsonMappingException | JsonParseException e) {
			e.printStackTrace();
			response.status(HttpServletResponse.SC_BAD_REQUEST);
			return e.getMessage();
		}
	}
}
