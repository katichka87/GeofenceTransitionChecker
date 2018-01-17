package com.kateryna.testassignment.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.location.places.ui.PlacePicker
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kateryna.testassignment.R
import com.kateryna.testassignment.TestAssignmentApp
import com.kateryna.testassignment.adapters.EventType
import com.kateryna.testassignment.device.GeofenceStateReceiver
import com.kateryna.testassignment.interfcaces.ViewInterface
import com.kateryna.testassignment.model.TransitionEvent
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ViewInterface {

    @Inject lateinit var presenter: Presenter
    @Inject lateinit var receiver: GeofenceStateReceiver
    private val PLACE_PICKER_REQUEST = 1
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private var locationPermissionSubject: PublishSubject<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as TestAssignmentApp).appComponent.inject(this)
        presenter.view = this
        registerReceiver(receiver, IntentFilter("com.kateryna.testassignment.GEOFENCE_TRANSITION_EVENT"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this)
                val toastMsg = String.format("Place: %s", place.name)
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()

                latText.accept(place.latLng.latitude.toString())
                lngText.accept(place.latLng.longitude.toString())
            }
        }
    }

    override val startPlacePicker
        get() = Consumer<Any> {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }

    override val latText
        get() = RxTextView.text(lat_edit_field)

    override val lngText
        get() = RxTextView.text(lng_edit_field)

    override val latChanges
        get() = RxTextView.textChanges(lat_edit_field).share()

    override val lngChanges
        get() = RxTextView.textChanges(lng_edit_field).share()

    override val radiusChanges
        get() = RxTextView.textChanges(radius_edit_field).share()

    override val setGeofenceClicks
        get() = RxView.clicks(set_geofence_btn)

    override val setGeofenceEnabled
        get() = RxView.enabled(set_geofence_btn)

    override val pickLocationClicks
        get() = RxView.clicks(place_pick_btn)

    override val setGeofenceTransition
        get() = RxTextView.text(result)

    override val requestLocationPermission: Observable<Boolean>
        get() = permissions()

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun permissions(): Observable<Boolean> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return Observable.just(true)
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
        locationPermissionSubject?.onComplete()
        locationPermissionSubject = null
        locationPermissionSubject = PublishSubject.create<Boolean>()
        return locationPermissionSubject!!
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && locationPermissionSubject != null) {
            locationPermissionSubject?.onNext(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            locationPermissionSubject?.onComplete()
            locationPermissionSubject = null
        }
    }
}
