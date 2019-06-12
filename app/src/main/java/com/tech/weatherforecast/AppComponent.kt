package com.tech.weatherforecast

import com.tech.weatherforecast.features.weatherforecast.di.featureModule
import com.tech.weatherforecast.local.di.localModule
import com.tech.weatherforecast.remote.di.createRemoteModule
import com.tech.weatherforecast.repository.di.repositoryModule
import com.tech.weatherforecast.services.di.workerModule


val appComponent= listOf(
    createRemoteModule("https://api.openweathermap.org/data/2.5/", "5ad7218f2e11df834b0eaf3a33a39d2a"),
    workerModule, repositoryModule, featureModule, localModule)