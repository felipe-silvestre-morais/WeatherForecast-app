package com.tech.weatherforecast.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tech.weatherforecast.local.converter.Converters
import com.tech.weatherforecast.local.dao.WeatherForecastDao
import com.tech.weatherforecast.model.WeatherForecast

@Database(entities = [WeatherForecast::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    // DAO
    abstract fun weatherDao(): WeatherForecastDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app.db")
                .build()
    }
}