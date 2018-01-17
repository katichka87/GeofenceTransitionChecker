package com.kateryna.testassignment.adapters

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.util.Log
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
class GeofenceAdapter(val geofencingClient: GeofencingClient, val geofenceIntent: PendingIntent, val geofenceEnterExitEvents: Observable<TransitionEvent>) {

    private fun buildGeofence(geofence: GeofenceModel) = Geofence.Builder()
            .setRequestId(geofence.key)
            .setCircularRegion(geofence.latLng.latitude, geofence.latLng.longitude, geofence.radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setNotificationResponsiveness(1000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

    val registerGeofence
        @SuppressLint("MissingPermission")
        get() = Consumer { geofence: GeofenceModel ->
            val builder = GeofencingRequest.Builder()
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            builder.addGeofences(listOf(buildGeofence(geofence)))

            geofencingClient.addGeofences(builder.build(), geofenceIntent)
                    .addOnSuccessListener {
                        Log.i(GeofenceAdapter::class.java.simpleName, "Succeed to register Geofence")
                    }
                    .addOnFailureListener {
                        Log.e(GeofenceAdapter::class.java.simpleName, "Failed to register Geofence")
                    }
        }

    val unregisterGeofence
        get() = Consumer { geofence: GeofenceModel ->
            geofencingClient.removeGeofences(listOf(geofence.key))
        }

}