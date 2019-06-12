package com.tech.weatherforecast

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tech.weatherforecast.common_test.datasets.WeatherDataset
import com.tech.weatherforecast.features.weatherforecast.WeatherForecastViewModel
import com.tech.weatherforecast.features.weatherforecast.domain.GetWeatherForecastUseCase
import com.tech.weatherforecast.features.weatherforecast.service.ServiceStarter
import com.tech.weatherforecast.model.WeatherForecast
import io.mockk.*
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class WeatherForecastFeatureUnitTests {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testScheduler = TestScheduler()
    private lateinit var getWeatherForecastUseCase: GetWeatherForecastUseCase
    private lateinit var viewModel: WeatherForecastViewModel
    private val rxPermissions = mockk<RxPermissions>()
    private val serviceStarter = mockk<ServiceStarter>()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        getWeatherForecastUseCase = mockk()
        viewModel = WeatherForecastViewModel(getWeatherForecastUseCase, rxPermissions, serviceStarter)
    }

    @Test
    fun `Weather requested when Fragment is created with permission granted`() {
        val observerResult = mockk<Observer<WeatherForecast>>(relaxed = true)
        val events = MutableLiveData<WeatherForecast>()
        val result = WeatherDataset.FAKE_WEATHER.first()

        every { getWeatherForecastUseCase() } returns events
        every { serviceStarter.startService() } returns Unit
        every {
            rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } returns Observable.just(true)

        viewModel.weather.observeForever(observerResult)
        viewModel.loadData()
        testScheduler.triggerActions()

        events.value = result

        verify {
            serviceStarter.startService()
            observerResult.onChanged(WeatherDataset.FAKE_WEATHER.first())
        }

        confirmVerified(observerResult)
    }

    @Test
    fun `Weather requested when Fragment is created no permission granted`() {
        val observerResult = mockk<Observer<Any>>(relaxed = true)

        every {
            rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } returns Observable.just(false)

        viewModel.permissionDeniedDialog.observeForever(observerResult)
        viewModel.loadData()
        testScheduler.triggerActions()

        verify {
            serviceStarter wasNot Called
            observerResult.onChanged(any())
        }

        confirmVerified(observerResult)
    }
}