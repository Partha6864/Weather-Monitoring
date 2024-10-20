package com.example.weathermonitoring.controller;

import com.example.weathermonitoring.model.Weather;
import com.example.weathermonitoring.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    @Value("${api.key}")
    private String apiKey;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Weather getWeather(@RequestParam String city) {
        return weatherService.fetchWeather(city, apiKey);
    }
}
