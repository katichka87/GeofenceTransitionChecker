package com.kateryna.testassignment.interfcaces

import com.kateryna.testassignment.model.GeofenceModel
import com.kateryna.testassignment.model.TransitionEvent
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by kati4ka on 1/20/18.
 */
interface IGeofenceAdapter {
    val geofenceStatus: BehaviorSubject<TransitionEvent>
    val errorFlow: PublishSubject<Throwable>
    val registerGeofence: Consumer<GeofenceModel>
    val unregisterGeofence: Consumer<GeofenceModel>
}