package com.tech.weatherforecast.remote

import com.tech.weatherforecast.remote.base.BaseTest
import com.tech.weatherforecast.remote.response.WeatherResponse
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection

class WeatherServiceTest: BaseTest() {

    @Test
    fun `fetch weather`() {
        // GIVEN
        val response = mockHttpResponse(
            mockServer,
            "weather.json",
            WeatherResponse::class.java,
            HttpURLConnection.HTTP_OK)

        // WHEN
        val testSubscriber = weatherService.fetchWeatherAsync("", 0.0, 0.0).test()

        // THEN
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(response)
    }

    @Test
    fun `fetch weather fail`() {
        // GIVEN
        mockHttpResponse(mockServer,"weather.json", WeatherResponse::class.java, HttpURLConnection.HTTP_FORBIDDEN)

        // WHEN
        val testSubscriber = weatherService.fetchWeatherAsync("", 0.0, 0.0).test()

        // THEN
        testSubscriber.assertFailure(HttpException::class.java)
    }
}