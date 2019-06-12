package com.tech.weatherforecast.remote

import com.tech.weatherforecast.remote.response.WeatherResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun fetchWeatherAsync(@Query("appid") apiKey: String,
                         @Query("lat") lat: Double,
                         @Query("lon") lon: Double,
                         @Query("units") units: String = "metric"): Flowable<WeatherResponse>

}