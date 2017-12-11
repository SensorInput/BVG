package de.htw.ai.busbunching.transformer

import com.fasterxml.jackson.databind.ObjectMapper
import spark.ResponseTransformer

/**
  * Created by tobias on 05.02.17.
  */
class JsonTransformer extends ResponseTransformer {

	private val objectMapper = new ObjectMapper()

	override def render(o: scala.Any): String = {
		objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o)
	}
}