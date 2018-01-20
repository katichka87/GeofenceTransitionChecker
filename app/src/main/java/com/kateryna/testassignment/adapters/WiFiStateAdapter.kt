package com.kateryna.testassignment.adapters

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.kateryna.testassignment.device.NetworkChangeReceiver
import com.kateryna.testassignment.interfcaces.IWiFiStateAdapter
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by kati4ka on 1/19/18.
 */
open class WiFiStateAdapter(networkChangeReceiver: NetworkChangeReceiver, private val context: Context): IWiFiStateAdapter {

    override open val wiFiNameObservable: BehaviorSubject<String> = BehaviorSubject.createDefault(getWifiName(context))
    init {
        networkChangeReceiver.networkNameChange.map { getWifiName(context) }.subscribe { wiFiNameObservable.onNext(it) }
    }

    private fun getWifiName(context: Context): String {
        val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (manager.isWifiEnabled) {
            val wifiInfo = manager.connectionInfo
            if (wifiInfo != null) {
                val state = WifiInfo.getDetailedStateOf(wifiInfo.supplicantState)
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.ssid
                }
            }
        }
        return ""
    }

}