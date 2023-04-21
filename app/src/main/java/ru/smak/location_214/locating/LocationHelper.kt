package ru.smak.location_214.locating

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class LocationHelper(
    private val locationUpdater: (Location)->Unit
) : LocationListener{
    var flp: FusedLocationProviderClient? = null

    @SuppressLint("MissingPermission")
    fun start(context: Context){
        flp = flp ?: LocationServices.getFusedLocationProviderClient(
            context.applicationContext
        )
        if (/*Проверить наличие разрешений*/true){
            try {
                flp?.run{
                    getLastLocation(
                        LastLocationRequest.Builder()
                            .setGranularity(Granularity.GRANULARITY_FINE)
                            .setMaxUpdateAgeMillis(7_200_000L)
                            .build()
                    )
                    requestLocationUpdates(
                        LocationRequest.Builder(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            10_000,
                        ).setMinUpdateIntervalMillis(5_000)
                            .setMinUpdateDistanceMeters(30f)
                            .build(),
                        Dispatchers.Default.asExecutor(),
                        this@LocationHelper
                    )
                }
            } catch (e: Throwable){

            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        locationUpdater(p0)
    }
}