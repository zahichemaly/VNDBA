package com.booboot.vndbandroid.service

import android.net.ConnectivityManager
import android.net.Network
import com.booboot.vndbandroid.api.VNDBServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectionReceiver : ConnectivityManager.NetworkCallback() {
    private var startupCall = true

    override fun onLost(network: Network?) {
        GlobalScope.launch {
            VNDBServer.closeAll()
        }
    }

    override fun onAvailable(network: Network?) {
        if (startupCall) {
            startupCall = false
        } else GlobalScope.launch {
            VNDBServer.closeAll()
        }
    }
}