package com.kateryna.testassignment.dagger

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.kateryna.testassignment.TestAssignmentApp
import com.kateryna.testassignment.adapters.GeofenceAdapter
import com.kateryna.testassignment.device.GeofenceStateReceiver
import com.kateryna.testassignment.device.GeofenceTransitionsIntentService
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Singleton

/**
 * Created by kati4ka on 1/16/18.
 */
@Module
class AppModule(val app: TestAssignmentApp) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun client(context: Context): GeofencingClient = LocationServices.getGeofencingClient(context)

    @Provides
    @Singleton
    fun intent(context: Context): PendingIntent = PendingIntent.getService(context, 0,
            Intent(context, GeofenceTransitionsIntentService::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

    @Provides
    @Singleton
    fun receiver(): GeofenceStateReceiver = GeofenceStateReceiver()

    @Provides
    @Singleton
    fun geofenceAdapter(geofencingClient: GeofencingClient, geofenceIntent: PendingIntent, receiver: GeofenceStateReceiver): GeofenceAdapter =
            GeofenceAdapter(geofencingClient, geofenceIntent, receiver.stateChanges, Observable.just(true))
}