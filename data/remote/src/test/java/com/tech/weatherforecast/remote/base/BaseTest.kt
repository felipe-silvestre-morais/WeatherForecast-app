package com.tech.weatherforecast.remote.base

import com.tech.weatherforecast.remote.WeatherService
import com.tech.weatherforecast.remote.di.createRemoteModule
import com.google.gson.Gson
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import java.io.File

abstract class BaseTest: KoinTest {

    protected val weatherService: WeatherService by inject()
    protected lateinit var mockServer: MockWebServer
    private val gson = Gson()

    @Before
    open fun setUp(){
        this.configureMockServer()
        this.configureDi()
    }

    @After
    open fun tearDown(){
        this.stopMockServer()
        StandAloneContext.stopKoin()
    }

    // CONFIGURATION
    private fun configureDi(){
        StandAloneContext.startKoin(listOf(createRemoteModule(mockServer.url("/").toString(), "")))
    }

    private fun configureMockServer(){
        mockServer = MockWebServer()
        mockServer.start()
    }

    private fun stopMockServer() {
        mockServer.shutdown()
    }

    fun <T> mockHttpResponse(mockServer: MockWebServer, fileName: String, classOfT: Class<T>, responseCode: Int): T {
        val json = getJson(fileName)
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(json))

        return gson.fromJson(json, classOfT)
    }

    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}