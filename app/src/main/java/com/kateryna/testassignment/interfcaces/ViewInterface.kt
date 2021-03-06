package com.kateryna.testassignment.interfcaces

import android.app.Activity
import com.kateryna.testassignment.adapters.EventType
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

/**
 * Created by kati4ka on 1/17/18.
 */
interface ViewInterface {

    val latText: Consumer<in CharSequence>
    val lngText: Consumer<in CharSequence>
    val latChanges: Observable<CharSequence>
    val lngChanges: Observable<CharSequence>
    val radiusChanges: Observable<CharSequence>
    val wifiChanges: Observable<CharSequence>
    val setGeofenceClicks: Observable<Any>
    val setGeofenceEnabled: Consumer<in Boolean>
    val pickLocationClicks: Observable<Any>
    val requestLocationPermission: Observable<Boolean>
    val startPlacePicker: Consumer<Any>
    val setGeofenceTransition: Consumer<in EventType>
    val showError: Consumer<String>
}