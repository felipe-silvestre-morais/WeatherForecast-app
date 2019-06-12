package com.tech.weatherforecast.features.weatherforecast.di

import android.preference.PreferenceManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tech.weatherforecast.features.weatherforecast.MainFragment
import com.tech.weatherforecast.features.weatherforecast.WeatherForecastViewModel
import com.tech.weatherforecast.features.weatherforecast.domain.GetWeatherForecastUseCase
import com.tech.weatherforecast.features.weatherforecast.service.DefaultServiceStarter
import com.tech.weatherforecast.features.weatherforecast.service.ServiceStarter
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val featureModule = module {
    factory { GetWeatherForecastUseCase(get()) }
    factory { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    factory<ServiceStarter> { DefaultServiceStarter(androidContext(), get()) }
    viewModel { (fragment: MainFragment) -> WeatherForecastViewModel(get(), RxPermissions(fragment), get()) }
}