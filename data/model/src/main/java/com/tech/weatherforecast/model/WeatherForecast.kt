package com.tech.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
data class WeatherForecast (

    @PrimaryKey
    val id: Long,

    val name: String,

    val title: String,

    val description: String,

    val temperature: Double,

    val temperatureMin: Double,

    val temperatureMax: Double,

    var lastRefreshed: Date
)