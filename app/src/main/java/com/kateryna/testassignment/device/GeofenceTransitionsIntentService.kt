package com.kateryna.testassignment.device

import android.app.IntentService
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Error getting geofence transition ${geofencingEvent.errorCode}")
            // Send broadcast about error
            val broadcastIntent = Intent()
            broadcastIntent.action = "com.kateryna.testassignment.GEOFENCE_TRANSITION_EVENT"
            broadcastIntent.putExtra("error", geofencingEvent.errorCode)
            sendBroadcast(broadcastIntent)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Send the broadcast
            val broadcastIntent = Intent()
            broadcastIntent.action = "com.kateryna.testassignment.GEOFENCE_TRANSITION_EVENT"
            broadcastIntent.putExtra("geofence", triggeringGeofences[0].requestId)
            broadcastIntent.putExtra("eventType", geofenceTransition)
            sendBroadcast(broadcastIntent)
        }
    }
}
