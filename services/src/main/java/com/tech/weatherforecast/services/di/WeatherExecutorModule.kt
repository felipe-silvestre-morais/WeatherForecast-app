package com.tech.weatherforecast.services.di

import com.tech.weatherforecast.services.RefreshWeatherForecastWorker
import com.tech.weatherforecast.services.WeatherForecastExecutor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

val workerModule = module {
    factory { ReactiveLocationProvider(androidContext()) }
    factory { (worker: RefreshWeatherForecastWorker) -> WeatherForecastExecutor(get(), worker, get()) }
}