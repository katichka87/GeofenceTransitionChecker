package com.kateryna.testassignment.view

import com.google.android.gms.maps.model.LatLng
import com.kateryna.testassignment.adapters.EventType
import com.kateryna.testassignment.interfcaces.IGeofenceAdapter
import com.kateryna.testassignment.interfcaces.IWiFiStateAdapter
import com.kateryna.testassignment.interfcaces.ViewInterface
import com.kateryna.testassignment.model.GeofenceModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import javax.inject.Inject

/**
 * Created by kati4ka on 1/16/18.
 */
class Presenter @Inject constructor(private val adapter: IGeofenceAdapter, private val wiFiStateAdapter: IWiFiStateAdapter) {

    val GEOFENCE_KEY = "fence1"

    var lat: Double? = null
    var lng: Double? = null
    var radius: Float? = null
    var wiFiName: String? = null

    var view: ViewInterface? = null
        set(value) {
            value?.pickLocationClicks?.flatMap { value.requestLocationPermission }?.filter { it }?.subscribe(value.startPlacePicker)

            Observable.combineLatest(
                    value?.latChanges?.doOnNext { lat = if (it.isNotEmpty()) it.toString().toDouble() else null },
                    value?.lngChanges?.doOnNext { lng = if (it.isNotEmpty()) it.toString().toDouble() else null },
                    value?.radiusChanges?.doOnNext { radius = if (it.isNotEmpty()) it.toString().toFloat() else null },
                    value?.wifiChanges?.doOnNext { wiFiName = "\"${it}\"" },
                    Function4 { lat: CharSequence, lng: CharSequence, radius: CharSequence, wiFi: CharSequence -> lat.isNotEmpty() && lng.isNotEmpty() && radius.isNotEmpty() && wiFi.isNotEmpty() })
                    .subscribe(value?.setGeofenceEnabled)

            value?.setGeofenceClicks
                    ?.flatMap { value.requestLocationPermission.filter { it } }
                    ?.map { GeofenceModel(GEOFENCE_KEY, LatLng(lat.toString().toDouble(), lng.toString().toDouble()), radius.toString().toFloat()) }
                    ?.doOnNext(adapter.unregisterGeofence)
                    ?.subscribe(adapter.registerGeofence)

            Observable.combineLatest(adapter.geofenceStatus.map { it.event }, wiFiStateAdapter.wiFiNameObservable,
                    BiFunction { transitionType: EventType, connectedWiFiName: String -> if (wiFiName == connectedWiFiName) EventType.ENTER else transitionType })
                    .subscribe(value?.setGeofenceTransition)

            adapter.errorFlow.map { it.toString() }.subscribe()
        }
}