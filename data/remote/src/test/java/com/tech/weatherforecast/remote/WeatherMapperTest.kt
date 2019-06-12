package com.tech.weatherforecast.remote

import com.tech.weatherforecast.remote.response.Temperature
import com.tech.weatherforecast.remote.response.Weather
import com.tech.weatherforecast.remote.response.WeatherMapper
import com.tech.weatherforecast.remote.response.WeatherResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun `weather response map`() {
        // GIVEN
        val id = 0L
        val name = "Weather"
        val titleWeather = "Title"
        val titleDesc = "Desc"
        val listWeathers = listOf(Weather(titleWeather, titleDesc))
        val temp = 1.0
        val tempMin = 0.0
        val tempMax = 2.0
        val temperature = Temperature(temp, tempMin, tempMax)

        // WHEN
        val weatherResponse = WeatherResponse(id, name, listWeathers, temperature)
        val weatherForecast = WeatherMapper.map(weatherResponse)

        // THEN
        assertEquals(weatherForecast.name, name)
        assertEquals(weatherForecast.title, titleWeather)
        assertEquals(weatherForecast.description, titleDesc)
        assertEquals(weatherForecast.temperatureMin, tempMin, 0.0)
        assertEquals(weatherForecast.temperatureMax, tempMax, 0.0)
    }
}