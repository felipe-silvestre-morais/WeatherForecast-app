package com.tech.weatherforecast.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tech.weatherforecast.repository.util.FakeData
import com.tech.weatherforecast.repository.utils.Resource
import com.tech.weatherforecast.local.dao.WeatherForecastDao
import com.tech.weatherforecast.model.WeatherForecast
import com.tech.weatherforecast.remote.WeatherDatasource
import io.mockk.*
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest : BaseRepositoryTest() {

    // FOR DATA
    private val testScheduler = TestScheduler()
    private lateinit var observer: Observer<Resource<WeatherForecast>>
    private lateinit var weatherRepository: WeatherRepository
    private val weatherService = mockk<WeatherDatasource>()
    private val weatherDao = mockk<WeatherForecastDao>(relaxed = true)
    private val liveDataWeather = mockk<LiveData<WeatherForecast>>()
    private val fakeWeather = FakeData.createFakeWeather(1L)

    @Before
    override fun setUp() {
        super.setUp()
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        observer = mockk(relaxed = true)
        weatherRepository = WeatherRepositoryImpl(weatherService, weatherDao)
    }

    @Test
    fun `get weather from local cache database`() {
        // GIVEN
        every { liveDataWeather.value } returns fakeWeather
        every { weatherDao.getWeatherForecast(1L) } returns liveDataWeather

        // WHEN
        val live = weatherRepository.getWeatherForecast()

        // THEN
        assert(fakeWeather == live.value)
    }

    @Test
    fun `Get weather from server when no internet is available`() {
        // GIVEN
        val exception = Exception("Internet")
        every { weatherService.fetchWeatherAsync(1.0, 2.0) } returns Flowable.error(exception)
        every { liveDataWeather.value } returns fakeWeather
        every { weatherDao.getWeatherForecast(1L) } returns liveDataWeather

        // WHEN
        weatherRepository.refreshWeatherForecast(1.0, 2.0, true).observeForever(observer)
        testScheduler.triggerActions()

        // THEN
        verifyOrder {
            observer.onChanged(Resource.error(exception, null))
        }

        confirmVerified(observer)
    }


    @Test
    fun `Get weather from network`() {
        // GIVEN
        every { liveDataWeather.value } returns fakeWeather
        every { weatherService.fetchWeatherAsync(1.0, 2.0) } returns Flowable.just(fakeWeather)
        every { weatherDao.getWeatherForecast(1L) } returns liveDataWeather

        // WHEN
        weatherRepository.refreshWeatherForecast(1.0, 2.0, true).observeForever(observer)
        testScheduler.triggerActions()

        // THEN
        verifyOrder {
            weatherDao.save(fakeWeather)
            observer.onChanged(Resource.success(fakeWeather))
        }

        confirmVerified(observer)
    }
}