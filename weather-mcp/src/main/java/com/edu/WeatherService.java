package com.edu;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springaicommunity.mcp.annotation.McpProgressToken;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public record WeatherResponse(Current current, Hourly hourly) {
        public record Current(LocalDateTime time, int interval, double temperature_2m) {}
        public record Hourly(List<LocalDateTime> time, List<Double> temperature_2m, List<Double> precipitation) {}
    }

    @McpTool(description = "Get the temperature (in celsius) for a specific location")
    public String getTemperature(
            McpSyncServerExchange exchange, // (1)
            @McpToolParam(description = "The location latitude") double latitude,
            @McpToolParam(description = "The location longitude") double longitude,
            @McpProgressToken String progressToken) { // (2)
        logger.info("Get temperature for location: {} {}", latitude, longitude);

        exchange.loggingNotification(LoggingMessageNotification.builder() // (3)
                .level(LoggingLevel.DEBUG)
                .data("Call getTemperature Tool with latitude: " + latitude + " and longitude: " + longitude)
                .meta(Map.of()) // non null meta as a workaround for bug: ...
                .build());
        WeatherResponse weatherResponse = RestClient.create()
                .get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m&hourly=temperature_2m,precipitation",
                        latitude, longitude)
                .retrieve()
                .body(WeatherResponse.class);
        // 50% progress
        exchange.progressNotification(new ProgressNotification(progressToken, 0.5, 1.0, "Current weather task completed"));

        String response = """
			Current weather: %s°C at location: (%s, %s)		
			""".formatted( weatherResponse.current().temperature_2m(), latitude, longitude);
        response = response +  " Forecast for next days: " + formatHourlyWeather(weatherResponse);
        logger.info(response);
        // 100% progress
        exchange.progressNotification(new ProgressNotification(progressToken, 1.0, 1.0, "Task completed"));
        return response;
    }

    public String formatHourlyWeather(WeatherResponse forecastWeatherResponse) {
        var times = forecastWeatherResponse.hourly().time();
        var temps = forecastWeatherResponse.hourly().temperature_2m();
        var precipitations = forecastWeatherResponse.hourly().precipitation();
        if (times.size() != temps.size() || times.size() != precipitations.size()) {
            throw new IllegalStateException("Time, temperature and rain list sizes do not match");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            sb.append(times.get(i))
                    .append(" → ")
                    .append(temps.get(i))
                    .append("°C")
                    .append(" → Expected rain: ")
                    .append(precipitations.get(i))
                    .append("mm");
            if (i < times.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}