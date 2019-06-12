package com.tech.weatherforecast.features.weatherforecast

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tech.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.*

object MainFragmentBinding {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

    @BindingAdapter("app:date")
    @JvmStatic
    fun date(view: TextView, date: Date?) {
        date?.let {
            val formatted = dateFormat.format(it)
            view.text = formatted
        }
    }

    @BindingAdapter("app:temperature")
    @JvmStatic
    fun temperature(view: TextView, temperature: Double) {
        val temperatureFormat = temperature.toInt().toString()
        view.text = view.context.getString(R.string.weatherforecast_temp, temperatureFormat)
    }

    @BindingAdapter("app:weather_icon")
    @JvmStatic
    fun temperature(view: ImageView, weather: String?) {
        weather?.apply {
            val iconId = when {
                contains("Clear", true) -> R.drawable.ic_sunny
                contains("Rain", true) -> R.drawable.ic_drizzle
                contains("Snow", true) -> R.drawable.ic_snowflake
                contains("Thunderstorm", true) -> R.drawable.ic_storm
                contains("Clouds", true) -> R.drawable.ic_cloudy
                contains("Rain", true) -> R.drawable.ic_drizzle
                contains("Drizzle", true) -> R.drawable.ic_drizzle
                else -> R.drawable.ic_haze
            }

            view.setImageResource(iconId)
        }
    }
}