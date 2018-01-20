package com.kateryna.testassignment.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng
import com.kateryna.testassignment.adapters.EventType
import com.kateryna.testassignment.adapters.GeofenceTransitionException
import com.kateryna.testassignment.model.GeofenceModel
import com.kateryna.testassignment.model.TransitionEvent
import io.reactivex.subjects.PublishSubject

/**
 * Created by kati4ka on 1/16/18.
 */
class GeofenceStateReceiver: BroadcastReceiver() {
    val stateChanges = PublishSubject.create<TransitionEvent>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.hasExtra("error") == true) {
            stateChanges.onError(GeofenceTransitionException(intent.getIntExtra("error", -1)))
        } else {
            val geofence = intent?.getStringExtra("geofence") ?: ""
            val event = intent?.getIntExtra("eventType", 0)

            stateChanges.onNext(TransitionEvent(GeofenceModel(geofence, LatLng(0.0 ,0.0), 0f), when (event) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> EventType.ENTER
                Geofence.GEOFENCE_TRANSITION_EXIT -> EventType.EXIT
                Geofence.GEOFENCE_TRANSITION_DWELL -> EventType.DWELL
                else -> EventType.NOT_DETECED
            }))
        }
    }
}