package com.tech.weatherforecast.local

import com.tech.weatherforecast.common_test.datasets.WeatherDataset
import com.tech.weatherforecast.common_test.datasets.WeatherDataset.DATE_REFRESH
import com.tech.weatherforecast.common_test.extensions.blockingObserve
import com.tech.weatherforecast.model.WeatherForecast
import com.tech.weatherforecast.local.base.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class WeatherForecastDaoTest: BaseTest() {

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    @Throws(Exception::class)
    fun writeWeatherAndRead() {
        database.weatherDao().save(WeatherDataset.FAKE_WEATHER[0])

        val weather = database.weatherDao().getWeatherForecast(1).blockingObserve()
        compareTwoWeather(WeatherDataset.FAKE_WEATHER.first(), weather!!)
    }

    @Test
    fun saveWeather_DateMustChange() {
        database.weatherDao().save(WeatherDataset.FAKE_WEATHER[0])
        val weather = database.weatherDao().getWeatherForecast(1).blockingObserve()
        assertNotEquals(DATE_REFRESH, weather!!.lastRefreshed)
    }

    // ---

    private fun compareTwoWeather(weather: WeatherForecast, weatherTest: WeatherForecast){
        assertEquals(weather.name, weatherTest.name)
        assertEquals(weather.description, weatherTest.description)
        assertEquals(weather.temperature, weatherTest.temperature, 0.0)
        assertEquals(weather.temperatureMax, weatherTest.temperatureMax, 0.0)
        assertEquals(weather.temperatureMin, weatherTest.temperatureMin, 0.0)
        assertEquals(weather.lastRefreshed, weatherTest.lastRefreshed)
    }
}