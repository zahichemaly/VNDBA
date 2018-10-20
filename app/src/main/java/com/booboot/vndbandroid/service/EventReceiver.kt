package com.booboot.vndbandroid.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.booboot.vndbandroid.App

class EventReceiver(val activity: LifecycleOwner) : LifecycleObserver {
    private val receivers = mutableListOf<BroadcastReceiver>()

    init {
        activity.lifecycle.addObserver(this)
    }

    fun observe(events: Map<String, () -> Unit>) {
        events.forEach { (name, callback) ->
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    callback()
                }
            }

            receivers.add(receiver)
            LocalBroadcastManager.getInstance(App.context).registerReceiver(receiver, IntentFilter(name))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        activity.lifecycle.removeObserver(this)
        receivers.forEach {
            LocalBroadcastManager.getInstance(App.context).unregisterReceiver(it)
        }
    }

    companion object {
        fun send(name: String) {
            LocalBroadcastManager.getInstance(App.context).sendBroadcast(Intent(name))
        }
    }
}