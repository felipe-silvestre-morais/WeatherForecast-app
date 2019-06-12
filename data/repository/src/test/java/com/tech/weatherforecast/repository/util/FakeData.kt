package com.tech.weatherforecast.repository.util

import com.tech.weatherforecast.model.WeatherForecast
import java.util.*

object FakeData {

    fun createFakeWeather(id: Long): WeatherForecast {
        return WeatherForecast(
            id = id,
            name = "Name_$id",
            title = "Title$id",
            description = "Description_$id",
            temperature = 1.0,
            temperatureMin = 0.0,
            temperatureMax = 2.0,
            lastRefreshed = DATE_REFRESH)

    }

    private val DATE_REFRESH: Date = GregorianCalendar(2019, 5, 12).time
}