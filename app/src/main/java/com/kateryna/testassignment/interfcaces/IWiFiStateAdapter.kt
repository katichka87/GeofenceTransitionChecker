package com.kateryna.testassignment.interfcaces

import io.reactivex.subjects.BehaviorSubject

/**
 * Created by kati4ka on 1/20/18.
 */
interface IWiFiStateAdapter {
    val wiFiNameObservable: BehaviorSubject<String>
}