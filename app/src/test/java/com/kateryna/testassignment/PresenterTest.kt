package com.kateryna.testassignment

import com.google.android.gms.maps.model.LatLng
import com.kateryna.testassignment.adapters.EventType
import com.kateryna.testassignment.interfcaces.IGeofenceAdapter
import com.kateryna.testassignment.interfcaces.IWiFiStateAdapter
import com.kateryna.testassignment.interfcaces.ViewInterface
import com.kateryna.testassignment.model.GeofenceModel
import com.kateryna.testassignment.model.TransitionEvent
import com.kateryna.testassignment.view.Presenter
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PresenterTest {

    @get:Rule
    val rxRule = RxSchedulersOverrideRule()

    lateinit var presenter: Presenter
    lateinit var mockGeofenceAdapter: MockGeofenceAdapter
    lateinit var mockWiFiStateAdapter: MockWiFiStateAdapter

    //View mock
    val mockView = MockView()

    @Before
    fun setUp() {
        mockGeofenceAdapter = MockGeofenceAdapter()
        mockWiFiStateAdapter = MockWiFiStateAdapter()
        presenter = Presenter(mockGeofenceAdapter, mockWiFiStateAdapter)
        presenter.view = mockView
    }

    @Test
    fun setGeofenceBtnAvailability() {
        mockView.latChanges.onNext("33.44")
        assertFalse(mockView.setGeofenceBtnSEnabled!!)
        mockView.lngChanges.onNext("55.43")
        assertFalse(mockView.setGeofenceBtnSEnabled!!)
        mockView.radiusChanges.onNext("200")
        assertFalse(mockView.setGeofenceBtnSEnabled!!)
        mockView.wifiChanges.onNext("cityPark")
        assertTrue(mockView.setGeofenceBtnSEnabled!!)
        mockView.radiusChanges.onNext("")
        assertFalse(mockView.setGeofenceBtnSEnabled!!)
    }

    @Test
    fun settingUpTheGeofence() {
        val model = GeofenceModel("fence1", LatLng(33.44, 55.43), 200f)

        mockView.latChanges.onNext("33.44")
        mockView.lngChanges.onNext("55.43")
        mockView.radiusChanges.onNext("200")
        mockView.wifiChanges.onNext("cityPark")
        mockView.setGeofenceClicks.onNext(Unit)
        mockView.requestLocationPermission.onNext(true)
        assertTrue(compareGeofenceModels(model, mockGeofenceAdapter.model))
    }

    @Test
    fun displayGeofenceTRansition() {
        mockView.latChanges.onNext("33.44")
        mockView.lngChanges.onNext("55.43")
        mockView.radiusChanges.onNext("200")
        mockView.wifiChanges.onNext("cityPark")
        mockView.setGeofenceClicks.onNext(Unit)
        mockView.requestLocationPermission.onNext(true)

        mockWiFiStateAdapter.wiFiNameObservable.onNext("\"cityPark\"")
        val model = GeofenceModel("fence1", LatLng(33.44, 55.43), 200f)
        mockGeofenceAdapter.geofenceStatus.onNext(TransitionEvent(model, EventType.ENTER))
        assertTrue(mockView.transition == EventType.ENTER)

        mockGeofenceAdapter.geofenceStatus.onNext(TransitionEvent(model, EventType.EXIT))
        assertTrue(mockView.transition == EventType.ENTER)

        mockWiFiStateAdapter.wiFiNameObservable.onNext("any")
        assertTrue(mockView.transition == EventType.EXIT)
    }

    private fun compareGeofenceModels(expected: GeofenceModel, actual: GeofenceModel?) = actual?.latLng?.equals(expected.latLng) == true
            && actual.radius == expected.radius
            && actual.key == expected.key

    open class MockView: ViewInterface {
        var setGeofenceBtnSEnabled: Boolean? = false
        var transition: EventType? = null

        override val latText = Consumer<CharSequence> {  }
        override val lngText = Consumer<CharSequence> {  }
        override val latChanges = PublishSubject.create<CharSequence>()
        override val lngChanges = PublishSubject.create<CharSequence>()
        override val radiusChanges = PublishSubject.create<CharSequence>()
        override val wifiChanges = PublishSubject.create<CharSequence>()
        override val setGeofenceClicks = PublishSubject.create<Any>()
        override val setGeofenceEnabled = Consumer<Boolean> { setGeofenceBtnSEnabled = it }
        override val pickLocationClicks = PublishSubject.create<Any>()
        override val requestLocationPermission = PublishSubject.create<Boolean>()
        override val startPlacePicker = Consumer<Any> {  }
        override val setGeofenceTransition = Consumer<EventType> { transition = it }
        override val showError = Consumer<String> {  }
    }

    open class MockGeofenceAdapter: IGeofenceAdapter {
        var model: GeofenceModel? = null
        override val geofenceStatus = BehaviorSubject.create<TransitionEvent>()
        override val errorFlow = PublishSubject.create<Throwable>()
        override val registerGeofence = Consumer<GeofenceModel> {
            model = it
        }
        override val unregisterGeofence = Consumer<GeofenceModel> { model = null }
    }

    open class MockWiFiStateAdapter: IWiFiStateAdapter {
        override val wiFiNameObservable = BehaviorSubject.createDefault("")
    }
}
