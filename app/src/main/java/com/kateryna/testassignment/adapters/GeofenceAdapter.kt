package com.kateryna.testassignment.adapters

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.kateryna.testassignment.interfcaces.IGeofenceAdapter
import com.kateryna.testassignment.model.GeofenceModel
import com.kateryna.testassignment.model.TransitionEvent
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by kati4ka on 1/16/18.
 */
open class GeofenceAdapter(val geofencingClient: GeofencingClient, val geofenceIntent: PendingIntent,
                      geofenceEnterExitEvents: Observable<TransitionEvent>): IGeofenceAdapter {
    override val geofenceStatus = BehaviorSubject.create<TransitionEvent>()
    init {
        geofenceEnterExitEvents.doOnError { errorFlow.onNext(it) }.onErrorResumeNext(Observable.empty<TransitionEvent>()).subscribe { geofenceStatus.onNext(it) }
    }
    override val errorFlow: PublishSubject<Throwable> = PublishSubject.create<Throwable>()

    private fun buildGeofence(geofence: GeofenceModel) = Geofence.Builder()
            .setRequestId(geofence.key)
            .setCircularRegion(geofence.latLng.latitude, geofence.latLng.longitude, geofence.radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setNotificationResponsiveness(1000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(3000)
            .build()

    override val registerGeofence
        @SuppressLint("MissingPermission")
        get() = Consumer { geofence: GeofenceModel ->
            geofenceStatus.onNext(TransitionEvent(geofence, EventType.NOT_DETECED))
            val builder = GeofencingRequest.Builder()
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_DWELL or GeofencingRequest.INITIAL_TRIGGER_EXIT)
            builder.addGeofences(listOf(buildGeofence(geofence)))

            geofencingClient.addGeofences(builder.build(), geofenceIntent)
                    .addOnSuccessListener {
                        Log.i(GeofenceAdapter::class.java.simpleName, "Succeed to register Geofence")
                    }
                    .addOnFailureListener {
                        errorFlow.onNext(it)
                        Log.e(GeofenceAdapter::class.java.simpleName, "Failed to register Geofence")
                    }
        }

    override val unregisterGeofence
        get() = Consumer { geofence: GeofenceModel ->
            geofencingClient.removeGeofences(listOf(geofence.key))
                    .addOnSuccessListener {
                        Log.i(GeofenceAdapter::class.java.simpleName, "Succeed to remove Geofence")
                    }
                    .addOnFailureListener {
                        errorFlow.onNext(it)
                        Log.e(GeofenceAdapter::class.java.simpleName, "Failed to remove Geofence")
                    }
        }

}