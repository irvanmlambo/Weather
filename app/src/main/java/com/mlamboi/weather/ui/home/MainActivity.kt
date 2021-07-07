package com.mlamboi.weather.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.mlamboi.weather.data.model.Status
import com.mlamboi.weather.data.remote.repository.WeatherRepository
import com.mlamboi.weather.databinding.ActivityMainBinding
import com.mlamboi.weather.utils.LOCATION_REQUEST
import com.mlamboi.weather.utils.LocationProvider
import com.mlamboi.weather.utils.LocationUtil
import com.mlamboi.weather.utils.unixTimestampToTimeString

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var provider: LocationProvider
    private lateinit var repository: WeatherRepository

    private var isEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate view and obtain an instance of the binding class
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        provider = LocationProvider(this)
        repository = WeatherRepository()

        // Assign the component to a property in the binding class
        binding.viewModel = viewModel

        // Check user current location
        checkLocationStatus()

        // Observe view model
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    /**
     * Check location status
     */
    private fun checkLocationStatus() {
        LocationUtil(this).turnOnLocation(object : LocationUtil.ILocation {
            override fun locationStatus(isEnabled: Boolean) {
                this@MainActivity.isEnabled = isEnabled
            }
        })
    }

    /**
     * Observe view model live data
     */
    private fun observeViewModel() {
        viewModel.locationLiveData.observe(this, {
            viewModel.getWeatherInformation(
                repository,
                it.latitude.toString(),
                it.longitude.toString()
            )
        })

        viewModel.weatherLiveData.observe(this, {
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.model = it.data

                        binding.sunrise.text = it.data?.sys?.sunrise?.unixTimestampToTimeString()
                        binding.sunset.text = it.data?.sys?.sunset?.unixTimestampToTimeString()
                    }

                    Status.ERROR -> {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    fun onRefreshClicked(view: View){
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            !isEnabled -> Toast.makeText(applicationContext, "Please enable location access.", Toast.LENGTH_LONG).show()

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> requestLocationPermission()

            else -> requestLocationPermission()
        }
    }

    private fun startLocationUpdate() {
        viewModel.getCurrentLocation(provider)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST
        )
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }
}