package com.tech.weatherforecast.services

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.work.*
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class RefreshWeatherForecastWorker(private val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams), LocationPermissionChecker, KoinComponent {
    private val executor: WeatherForecastExecutor by inject { parametersOf(this@RefreshWeatherForecastWorker) }

    companion object {
        private const val WORK_TAG = "refresh_weather"

        fun scheduleWork(periodicIntervalInMinutes: Long, networkUnmetered: Boolean) {
            val requiredNetworkType = if (networkUnmetered) NetworkType.UNMETERED else NetworkType.CONNECTED

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(requiredNetworkType)  // if connected to WIFI
                .build()

            val workRequest = PeriodicWorkRequest
                    .Builder(
                        RefreshWeatherForecastWorker::class.java,
                        periodicIntervalInMinutes,
                        TimeUnit.MINUTES
                    )
                    .setConstraints(constraints)
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS)

            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, workRequest.build())
        }
    }

    override fun doWork(): Result {
        executor.executeUpdateWeatherForecast()
        return Result.success()
    }


    // -- Interface implementations

    override fun hasPermission(): Boolean
            = (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
}