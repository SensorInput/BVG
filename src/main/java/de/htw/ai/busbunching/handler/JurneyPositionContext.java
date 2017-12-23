package de.htw.ai.busbunching.handler;

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

public class JurneyPositionContext implements Route {

	private Settings settings;
	private ObjectMapper objectMapper;

	public JurneyPositionContext(Settings settings) {
		this.settings = settings;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {
		Connection connection = DatabaseUtils.createDatabaseConnection(settings);
		MeasurePointHandler measurePointHandler = new MeasurePointHandler(connection);

		MeasurePoint measurePoint = objectMapper.readValue(request.bodyAsBytes(), MeasurePoint.class);
		long id = measurePointHandler.importMeasurePoint(measurePoint);
		connection.close();

		response.status(HttpServletResponse.SC_CREATED);
		return id;
	}

}
