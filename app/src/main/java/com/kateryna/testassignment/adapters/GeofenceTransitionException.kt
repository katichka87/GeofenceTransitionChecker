package com.kateryna.testassignment.adapters

import com.google.android.gms.location.GeofenceStatusCodes

/**
 * Created by kati4ka on 1/20/18.
 */
class GeofenceTransitionException(val errorCode: Int): Throwable() {
    override fun toString() = when (errorCode) {
        GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "Geofence is not available"
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->  "Too many geofences"
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->  "Too many pending intents"
        else -> "Unknown geofence error"
    }
}