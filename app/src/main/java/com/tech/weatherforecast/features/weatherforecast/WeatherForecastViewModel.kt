package com.tech.weatherforecast.features.weatherforecast

import android.Manifest.permission
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tech.weatherforecast.features.common.SingleLiveEvent
import com.tech.weatherforecast.features.weatherforecast.domain.GetWeatherForecastUseCase
import com.tech.weatherforecast.features.weatherforecast.service.ServiceStarter
import com.tech.weatherforecast.model.WeatherForecast

class WeatherForecastViewModel(private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
                               private val rxPermissions: RxPermissions,
                               private val serviceStarter: ServiceStarter): ViewModel() {

    private var weatherSource: LiveData<WeatherForecast> = MutableLiveData()
    private val _weather = MediatorLiveData<WeatherForecast>()
    private val _permissionDeniedDialog = SingleLiveEvent<Any>()

    val weather: LiveData<WeatherForecast> get() = _weather
    val permissionDeniedDialog : LiveData<Any> get() = _permissionDeniedDialog

    // PUBLIC ACTIONS ---
    @SuppressLint("CheckResult")
    fun loadData() {
        // Must be done during an initialization phase like onCreate
        rxPermissions
            .request(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION)
            .subscribe { granted ->
                if (granted) {
                    serviceStarter.startService()
                    getWeatherForecast()
                } else {
                    _permissionDeniedDialog.call()
                }
            }
    }

    // ---

    private fun getWeatherForecast() {
        _weather.removeSource(weather) // We make sure there is only one source of livedata (allowing us properly refresh)
        weatherSource = getWeatherForecastUseCase()
        _weather.addSource(weatherSource) {
            _weather.value = it
        }
    }
}
