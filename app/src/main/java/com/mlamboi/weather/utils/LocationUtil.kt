package com.mlamboi.weather.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient

class LocationUtil(private val context: Context) {

    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val locationSettingsRequest: LocationSettingsRequest?
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationProvider.locationRequest)
        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    fun turnOnLocation(listener: ILocation?) {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            listener?.locationStatus(true)
        } else {
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    listener?.locationStatus(true)
                }
                .addOnFailureListener(context) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                            try {
                                val ex = e as ResolvableApiException
                                ex.startResolutionForResult(context, GPS_REQUEST)
                            } catch (sie: IntentSender.SendIntentException) {

                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Log.e(ContentValues.TAG, errorMessage)

                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    interface ILocation {
        fun locationStatus(isEnabled: Boolean)
    }
}
