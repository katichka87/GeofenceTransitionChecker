package com.kateryna.testassignment.adapters

import android.app.PendingIntent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.kateryna.testassignment.model.GeofenceModel
import com.kateryna.testassignment.model.TransitionEvent
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Created by kati4ka on 1/16/18.
 */
class GeofenceAdapter(val geofencingClient: GeofencingClient, val geofenceIntent: PendingIntent, val geofenceEnterExitEvents: Observable<TransitionEvent>, val permissionChecker: Observable<Boolean>) {

    private fun buildGeofence(geofence: GeofenceModel) = Geofence.Builder()
            .setRequestId(geofence.key)
            .setCircularRegion(geofence.latLng.latitude, geofence.latLng.longitude, geofence.radius)
            .setExpirationDuration(12 * 60 * 60 * 1000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

    val registerGeofence
        get() = Consumer { geofence: GeofenceModel ->
            val builder = GeofencingRequest.Builder()
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            builder.addGeofences(listOf(buildGeofence(geofence)))

            permissionChecker.filter { it }.subscribe {
                geofencingClient.addGeofences(builder.build(), geofenceIntent)
            }
        }

    val unregisterGeofence
        get() = Consumer { geofence: GeofenceModel ->
            geofencingClient.removeGeofences(listOf(geofence.key))
        }

}