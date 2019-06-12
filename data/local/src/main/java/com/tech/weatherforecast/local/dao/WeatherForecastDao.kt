package com.tech.weatherforecast.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.tech.weatherforecast.model.WeatherForecast
import java.util.*

@Dao
abstract class WeatherForecastDao: BaseDao<WeatherForecast>() {

    @Query("SELECT * FROM WeatherForecast WHERE id = :id LIMIT 1")
    abstract fun getWeatherForecast(id: Long): LiveData<WeatherForecast>

    /**
     * Each time we save a weather, we update its 'lastRefreshed' field
     * This allows us to know when we have to refresh its data
     */

    fun save(weather: WeatherForecast) {
        insert(weather.apply { lastRefreshed = Date() })
    }

}