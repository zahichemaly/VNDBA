package com.booboot.vndbandroid.util

import android.util.Log
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.api.VNDBServer

object Logger {
    fun log(sb: String) {
        if (!BuildConfig.DEBUG) return
        if (sb.length > 4000) {
            val chunkCount = sb.length / 4000     // integer division
            for (i in 0..chunkCount) {
                val max = 4000 * (i + 1)
                if (max >= sb.length) {
                    Log.e(VNDBServer.CLIENT, sb.substring(4000 * i))
                } else {
                    Log.e(VNDBServer.CLIENT, sb.substring(4000 * i, max))
                }
            }
        } else {
            Log.e(VNDBServer.CLIENT, sb)
        }
    }
}
