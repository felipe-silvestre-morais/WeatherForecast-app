package com.tech.weatherforecast.features.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tech.weatherforecast.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: com.tech.weatherforecast.databinding.ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

    }
}
