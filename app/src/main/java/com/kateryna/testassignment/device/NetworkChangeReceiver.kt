package com.kateryna.testassignment.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.subjects.PublishSubject

/**
 * Created by kati4ka on 1/19/18.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    val networkNameChange: PublishSubject<Any> = PublishSubject.create<Any>()

    override fun onReceive(context: Context, intent: Intent) {
        networkNameChange.onNext(Unit)
    }


}