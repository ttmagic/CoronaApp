package com.ttmagic.corona.util

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


class GpsUtils(private val context: Context) {

    companion object {
        const val REQUEST_CODE = 69
        private const val UPDATE_INTERVAL = (5 * 1000).toLong()  /* 5 secs */
        private const val FASTEST_INTERVAL: Long = 1000 /* 2 sec */
    }

    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true) //this is the key ingredient
    }

    // method for turn on GPS
    fun turnGPSOn(onEnabled: (() -> Unit)? = null) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onEnabled?.invoke()
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    //  GPS is already enable, callback GPS status through listener
                    onEnabled?.invoke()
                }
                .addOnFailureListener(context) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            (e as ResolvableApiException).startResolutionForResult(
                                context,
                                REQUEST_CODE
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
        }
    }

}