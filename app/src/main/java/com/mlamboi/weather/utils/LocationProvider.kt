package com.mlamboi.weather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mlamboi.weather.data.model.LocationData

class LocationProvider(
    context: Context
) : ILocationProvider{

    private var fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(callback: RequestCompleteListener<LocationData>) {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            it.also { callback.onRequestSuccess(setLocationData(it)) }
        } .addOnFailureListener {
            callback.onRequestFailure(it.localizedMessage)
        }

        startLocationUpdates()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun setLocationData(location: Location):LocationData {
        return LocationData(longitude = location.longitude, latitude = location.latitude)
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}

interface ILocationProvider {
    fun getCurrentLocation(callback:RequestCompleteListener<LocationData>)
}

interface RequestCompleteListener<T> {
    fun onRequestSuccess(data: T)
    fun onRequestFailure(message: String?)
}