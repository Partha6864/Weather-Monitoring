package com.example.weathermonitoring.service;

import com.example.weathermonitoring.dto.WeatherResponse;
import com.example.weathermonitoring.model.Weather;
import com.example.weathermonitoring.repository.WeatherRepository;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
    }

    public Weather fetchWeather(String city, String apiKey) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, apiKey);

        logger.info("Fetching weather data from URL: {}", url);

        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response == null) {
            logger.error("Received null response from OpenWeatherMap");
            throw new RuntimeException("Failed to fetch weather data");
        }

        logger.info("Weather response: {}", response);

        Weather weather = new Weather();
        weather.setCity(city);

        if (response.getWeather() != null && !response.getWeather().isEmpty()) {
            Map<String, Object> weatherInfo = response.getWeather().get(0);
            weather.setWeatherCondition((String) weatherInfo.get("description"));
        } else {
            logger.error("Weather data is null in response");
            throw new RuntimeException("Weather data is missing in the response");
        }

        if (response.getMain() == null) {
            logger.error("Main data is null in response");
            throw new RuntimeException("Main data is missing in the response");
        }

        weather.setTemperature(response.getMain().getTemp());
        weather.setFeelsLike(response.getMain().getFeels_like());
        weather.setHumidity(response.getMain().getHumidity());

        if (response.getWind() == null) {
            logger.error("Wind data is null in response");
            throw new RuntimeException("Wind data is missing in the response");
        }

        double windSpeedInMetersPerSecond = response.getWind().getSpeed();
        weather.setWindSpeed(windSpeedInMetersPerSecond);

        return weatherRepository.save(weather);
    }
}
