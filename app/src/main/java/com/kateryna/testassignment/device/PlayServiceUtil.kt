package com.kateryna.testassignment.device

import android.app.Activity
import android.net.wifi.WifiConfiguration
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.subjects.PublishSubject

/**
 * Created by kati4ka on 1/20/18.
 */
class PlayServiceUtil {
    val GOOGLE_SERVICES_REQUEST_CODE = 100

    fun checkGoogleServices(activity: Activity) {
        val result = PublishSubject.create<WifiConfiguration.Status>()
        try {
            val servicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
            if (servicesAvailable != ConnectionResult.SUCCESS) {
                if (GoogleApiAvailability.getInstance().isUserResolvableError(servicesAvailable)) {
                    GoogleApiAvailability.getInstance().getErrorDialog(activity, servicesAvailable, GOOGLE_SERVICES_REQUEST_CODE).show()
                } else {
                    val t = GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(activity)
                    t.addOnFailureListener {
                        Log.e(PlayServiceUtil::class.java.simpleName, "Failed to make Google services available")
                    }
                }
            }
        } catch (th: Throwable) {
            Log.e(PlayServiceUtil::class.java.simpleName, "Failed to check if Google services available", th)
            result.onError(th)
        }
    }
}