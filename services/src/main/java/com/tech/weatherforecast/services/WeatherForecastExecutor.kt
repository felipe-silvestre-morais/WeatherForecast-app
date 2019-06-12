package com.tech.weatherforecast.services

import android.annotation.SuppressLint
import android.util.Log
import com.tech.weatherforecast.repository.WeatherRepository
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.concurrent.TimeUnit

class WeatherForecastExecutor(
    private val locationProvider: ReactiveLocationProvider,
    private val locationPermissionChecker: LocationPermissionChecker,
    private val repository: WeatherRepository) {

    fun executeUpdateWeatherForecast() {
        if (locationPermissionChecker.hasPermission()) {
            retrieveAddressAndRefreshWeather()
        }
    }

    @SuppressLint("CheckResult", "MissingPermission")
    private fun retrieveAddressAndRefreshWeather() {
        locationProvider.lastKnownLocation
            .timeout(1, TimeUnit.MINUTES)
            .subscribe({
                repository.refreshWeatherForecast(it.latitude, it.longitude)
            }, {
                Log.e(WeatherForecastExecutor::javaClass.toString(), "Location provider error", it)
            })
    }
}