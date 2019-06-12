package com.tech.weatherforecast.repository

import androidx.lifecycle.LiveData
import com.tech.weatherforecast.repository.utils.NetworkBoundResource
import com.tech.weatherforecast.repository.utils.Resource
import com.tech.weatherforecast.local.dao.WeatherForecastDao
import com.tech.weatherforecast.model.WeatherForecast
import com.tech.weatherforecast.remote.WeatherDatasource
import io.reactivex.Flowable

interface WeatherRepository {
    fun getWeatherForecast(): LiveData<WeatherForecast>
    fun refreshWeatherForecast(lat: Double, lon: Double, forceRefresh: Boolean = false)
            : LiveData<Resource<WeatherForecast>>
}

class WeatherRepositoryImpl(private val datasource: WeatherDatasource,
                            private val dao: WeatherForecastDao): WeatherRepository {

    override fun getWeatherForecast(): LiveData<WeatherForecast> {
        return dao.getWeatherForecast(1L) // Get the last weather record from local database
    }

    /**
     * Check if it should request or not a fresh [WeatherForecast]
     * Once requested, persists it in local database
     * [NetworkBoundResource] is responsible to handle this behavior.
     */
    override fun refreshWeatherForecast(lat: Double, lon: Double, forceRefresh: Boolean)
            : LiveData<Resource<WeatherForecast>> {

        return object : NetworkBoundResource<WeatherForecast, WeatherForecast>() {

            override fun processResponse(response: WeatherForecast): WeatherForecast = response

            override fun saveCallResult(item: WeatherForecast)
                    = dao.save(item)

            override fun shouldFetch(data: WeatherForecast?): Boolean
                    = data == null || forceRefresh

            override fun loadFromDb(): LiveData<WeatherForecast>
                    = dao.getWeatherForecast(1L)

            override fun createCallAsync(): Flowable<WeatherForecast>
                    = datasource.fetchWeatherAsync(lat, lon)

        }.build().asLiveData()
    }
}