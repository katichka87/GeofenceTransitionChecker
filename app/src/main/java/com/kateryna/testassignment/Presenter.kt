package com.kateryna.testassignment

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.kateryna.testassignment.adapters.GeofenceAdapter
import com.kateryna.testassignment.model.GeofenceModel
import javax.inject.Inject

/**
 * Created by kati4ka on 1/16/18.
 */
class Presenter @Inject constructor(private val adapter: GeofenceAdapter) {

    fun setGeofence(latLng: LatLng, radius: Float) {
        Log.i("Presenter", "setGeofence")
        adapter.registerGeofence.accept(GeofenceModel("", LatLng(0.0, 0.0), 0f))
    }
}