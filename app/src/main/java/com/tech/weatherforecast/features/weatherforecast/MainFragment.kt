package com.tech.weatherforecast.features.weatherforecast

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tech.weatherforecast.R
import org.koin.android.viewmodel.ext.android.viewModel
import com.tech.weatherforecast.databinding.FragmentMainBinding
import org.koin.core.parameter.parametersOf

class MainFragment : Fragment() {

    // FOR DATA
    private val viewModel: WeatherForecastViewModel by viewModel { parametersOf(this@MainFragment) }
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.permissionDeniedDialog.observe(this, Observer {
            val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.weatherforecast_no_location_permission)
                .setMessage(R.string.weatherforecast_no_location_permission_message)
                .setPositiveButton(android.R.string.ok) { dialogInterface, _ -> dialogInterface.dismiss() }

            dialog.show()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadData()
    }
}
