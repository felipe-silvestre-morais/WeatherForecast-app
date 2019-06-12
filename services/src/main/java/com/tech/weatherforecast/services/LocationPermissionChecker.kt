package com.tech.weatherforecast.services

interface LocationPermissionChecker {
    fun hasPermission() : Boolean
}