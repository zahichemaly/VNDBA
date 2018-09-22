package com.booboot.vndbandroid.service

import android.net.ConnectivityManager
import android.net.Network
import com.booboot.vndbandroid.api.VNDBServer

class ConnectionReceiver : ConnectivityManager.NetworkCallback() {
    private var startupCall = true

    override fun onLost(network: Network?) {
        VNDBServer.closeAll().subscribe()
    }

    override fun onAvailable(network: Network?) {
        if (startupCall) {
            startupCall = false
        } else {
            VNDBServer.closeAll().subscribe()
        }
    }
}