package com.tech.weatherforecast.remote.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("weather")
    val weathers: List<Weather>,

    @SerializedName("main")
    val temperature: Temperature
)

data class Weather(

    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String

)

data class Temperature(
    @SerializedName("temp")
    val temp: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double
)