package com.tech.weatherforecast.features.weatherforecast.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tech.weatherforecast.model.WeatherForecast
import com.tech.weatherforecast.repository.WeatherRepository

/**
 * Use case that gets a [LiveData<WeatherForecast>]
 * and makes some specific logic actions on it.
 *
 * In this Use Case, I'm just doing nothing... ¯\_(ツ)_/¯
 */
class GetWeatherForecastUseCase(private val repository: WeatherRepository) {

    operator fun invoke(): LiveData<WeatherForecast> {
        return Transformations.map(repository.getWeatherForecast()) {
            it
        }
    }
}