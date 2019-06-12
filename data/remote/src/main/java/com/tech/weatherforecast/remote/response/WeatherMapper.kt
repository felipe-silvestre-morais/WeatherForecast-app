package com.tech.weatherforecast.remote.response

import com.tech.weatherforecast.model.WeatherForecast
import java.util.*

object WeatherMapper {
    fun map(weatherResponse: WeatherResponse): WeatherForecast {
        weatherResponse.apply {
            return WeatherForecast(
                1L, // We just want to show the result of the last response, we keep only one record in local database
                name,
                weathers.first().main,
                weathers.first().description,
                temperature.temp,
                temperature.tempMin,
                temperature.tempMax,
                Date()
            )
        }
    }
}