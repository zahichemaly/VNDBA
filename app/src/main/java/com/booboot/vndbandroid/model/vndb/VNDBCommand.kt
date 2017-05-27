package com.booboot.vndbandroid.model.vndb

import java.io.Serializable

abstract class VNDBCommand : Serializable {
    companion object {
        private val CLASSES = mapOf(
                "login" to Login::class.java,
                "error" to Error::class.java,
                "results" to Results::class.java,
                "dbstats" to DbStats::class.java
        )

        fun getClass(command: String) = CLASSES[command]
    }
}
