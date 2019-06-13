package com.tech.weatherforecast.features.weatherforecast.service

import android.content.Context
import android.content.SharedPreferences
import com.tech.weatherforecast.R
import com.tech.weatherforecast.services.RefreshWeatherForecastWorker

interface ServiceStarter {
    fun startService()
}

class DefaultServiceStarter(private val ctx: Context, private val preferences: SharedPreferences): ServiceStarter {
    private val prefServiceSchedule = "PREF_SERVICE_SCHEDULED"

    override fun startService() {
        val hasScheduledAlready = preferences.getBoolean(prefServiceSchedule, false)

        if (!hasScheduledAlready) {
            RefreshWeatherForecastWorker.scheduleWork(
                ctx.resources.getInteger(R.integer.refresh_weather_service_execution_in_minutes).toLong(), true)

            preferences.edit().putBoolean(prefServiceSchedule, true).apply()
        }
    }
}