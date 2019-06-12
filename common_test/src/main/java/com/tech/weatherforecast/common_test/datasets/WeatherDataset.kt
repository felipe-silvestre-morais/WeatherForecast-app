package com.tech.weatherforecast.common_test.datasets

import com.tech.weatherforecast.model.WeatherForecast
import java.util.*

object WeatherDataset {

    val DATE_REFRESH: Date = GregorianCalendar(2019, 5, 12).time
    val FAKE_WEATHER = listOf(
        WeatherForecast(1L, "WEATHER_1", "TITLE_1", "DESCRIPTION_1", 1.0, -1.0, 2.0, DATE_REFRESH),
        WeatherForecast(2L, "WEATHER_2", "TITLE_2","DESCRIPTION_2", 2.0, -2.0, 3.0, DATE_REFRESH),
        WeatherForecast(3L, "WEATHER_3", "TITLE_3","DESCRIPTION_3", 3.0, -3.0, 4.0, DATE_REFRESH)
    )
}