package com.tech.weatherforecast.remote

import com.tech.weatherforecast.remote.response.WeatherMapper
import com.tech.weatherforecast.model.WeatherForecast
import io.reactivex.Flowable

/**
 * Implementation of [WeatherService] interface
 */
class WeatherDatasource(private val weatherService: WeatherService, private val apiKey: String) {

    fun fetchWeatherAsync(lat: Double, lon: Double) : Flowable<WeatherForecast> =
        weatherService.fetchWeatherAsync(apiKey, lat, lon).map {
            WeatherMapper.map(it)
        }
}