package com.kateryna.testassignment.device

import android.app.IntentService
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.kateryna.testassignment.R
import com.google.android.gms.location.GeofenceStatusCodes
import android.text.TextUtils





class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = getErrorString(this, geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Send the broadcast
            val broadcastIntent = Intent()
            broadcastIntent.action = "com.kateryna.testassignment.GEOFENCE_TRANSITION_EVENT"
            broadcastIntent.putExtra("geofence", triggeringGeofences[0].requestId)
            broadcastIntent.putExtra("eventType", geofenceTransition)
            sendBroadcast(broadcastIntent)
        }
    }

    fun getErrorString(context: Context, errorCode: Int): String {
        return ""
        /*val mResources = context.getResources()
        when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> return mResources.getString(R.string.geofence_not_available)
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> return mResources.getString(R.string.geofence_too_many_geofences)
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> return mResources.getString(R.string.geofence_too_many_pending_intents)
            else -> return mResources.getString(R.string.unknown_geofence_error)
        }*/
    }


}
