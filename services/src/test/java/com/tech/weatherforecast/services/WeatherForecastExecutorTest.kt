package com.tech.weatherforecast.services

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.tech.weatherforecast.model.WeatherForecast
import com.tech.weatherforecast.repository.WeatherRepository
import com.tech.weatherforecast.repository.utils.Resource
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

class WeatherForecastExecutorTest {

    // FOR DATA
    private val testScheduler = TestScheduler()
    private lateinit var executor: WeatherForecastExecutor

    private val weatherRepository = mockk<WeatherRepository>()
    private val locationProvider = mockk<ReactiveLocationProvider>()
    private val permissionChecker = mockk<LocationPermissionChecker>()
    private val location = mockk<Location>()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        executor = WeatherForecastExecutor(locationProvider,permissionChecker,weatherRepository )
    }

    @Test
    fun `no location permission granted`() {
        // GIVEN
        every { permissionChecker.hasPermission() } returns false

        // WHEN
        executor.executeUpdateWeatherForecast()

        // THEN
        verify {
            permissionChecker.hasPermission()
            locationProvider wasNot Called
        }
    }

    @Test
    fun `error in delivering last known location`() {
        // GIVEN
        val exception = Exception("some error")
        every { permissionChecker.hasPermission() } returns true
        every { locationProvider.lastKnownLocation } returns Observable.error(exception)

        // WHEN
        executor.executeUpdateWeatherForecast()
        testScheduler.triggerActions()

        // THEN
        verifyOrder {
            permissionChecker.hasPermission()
            locationProvider.lastKnownLocation
            weatherRepository wasNot Called
        }
    }

    @Test
    fun `repository successfully called`() {
        // GIVEN
        val latitude = 1.0
        val longitude = 2.0
        val result = mockk<LiveData<Resource<WeatherForecast>>>()
        every { location.latitude } returns latitude
        every { location.longitude } returns longitude
        every { permissionChecker.hasPermission() } returns true
        every { locationProvider.lastKnownLocation } returns Observable.just(location)
        every { weatherRepository.refreshWeatherForecast(latitude, longitude) } returns result

        // WHEN
        executor.executeUpdateWeatherForecast()
        testScheduler.triggerActions()

        // THEN
        verifyOrder {
            location.latitude
            location.longitude
            weatherRepository.refreshWeatherForecast(latitude, longitude)
        }
    }
}