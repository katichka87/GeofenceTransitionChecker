package com.kateryna.testassignment.view

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.kateryna.testassignment.adapters.EventType
import com.kateryna.testassignment.adapters.GeofenceAdapter
import com.kateryna.testassignment.interfcaces.ViewInterface
import com.kateryna.testassignment.model.GeofenceModel
import io.reactivex.Observable
import io.reactivex.functions.Function3
import javax.inject.Inject

/**
 * Created by kati4ka on 1/16/18.
 */
class Presenter @Inject constructor(private val adapter: GeofenceAdapter) {

    val GEOFENCE_KEY = "fence1"

    var lat: Double? = null
    var lng: Double? = null
    var radius: Float? = null

    var view: ViewInterface? = null
        set(value) {
            value?.pickLocationClicks?.flatMap { value.requestLocationPermission }?.map { it }?.subscribe(value.startPlacePicker)

            Observable.combineLatest(
                    value?.latChanges?.doOnNext { lat = if (it.isNotEmpty()) it.toString().toDouble() else null },
                    value?.lngChanges?.doOnNext { lng = if (it.isNotEmpty()) it.toString().toDouble() else null },
                    value?.radiusChanges?.doOnNext { radius = if (it.isNotEmpty()) it.toString().toFloat() else null },
                    Function3 { lat: CharSequence, lng: CharSequence, radius: CharSequence -> lat.isNotEmpty() && lng.isNotEmpty() && radius.isNotEmpty() })
                    .subscribe(value?.setGeofenceEnabled)

            value?.setGeofenceClicks
                    ?.map { "" }
                    ?.doOnNext(value?.setGeofenceTransition)
                    ?.flatMap { value.requestLocationPermission.map { it } }
                    ?.map { GeofenceModel(GEOFENCE_KEY, LatLng(lat.toString().toDouble(), lng.toString().toDouble()), radius.toString().toFloat()) }
                    ?.doOnNext(adapter.unregisterGeofence)
                    ?.subscribe(adapter.registerGeofence)

            adapter.geofenceEnterExitEvents.map { if (it.event == EventType.ENTER)
                "You are inside the geofence" else "You are outside the geofence" }.subscribe(value?.setGeofenceTransition)

        }
}