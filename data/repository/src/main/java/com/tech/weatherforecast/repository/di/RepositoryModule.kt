package com.tech.weatherforecast.repository.di

import com.tech.weatherforecast.repository.WeatherRepository
import com.tech.weatherforecast.repository.WeatherRepositoryImpl
import org.koin.dsl.module.module

val repositoryModule = module {
    factory { WeatherRepositoryImpl(get(), get()) as WeatherRepository }
}