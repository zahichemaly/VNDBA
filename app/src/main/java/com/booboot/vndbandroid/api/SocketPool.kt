package com.booboot.vndbandroid.api

import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Response
import io.reactivex.SingleEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import javax.net.ssl.SSLSocket

object SocketPool {
    val MAX_SOCKETS = 5
    private val SOCKETS = arrayOfNulls<SSLSocket>(MAX_SOCKETS)
    private val LOCKS: ConcurrentMap<Int, Int> = ConcurrentHashMap()
    var throttleHandlingSocket = -1

    fun <T> getSocket(server: VNDBServer, options: Options, emitter: SingleEmitter<Response<T>>): SSLSocket? {
        options.socketIndex %= SOCKETS.size
        server.login(options, emitter)
        return SOCKETS[options.socketIndex]
    }

    fun setSocket(socketIndex: Int, socket: SSLSocket?) {
        SOCKETS[socketIndex % SOCKETS.size] = socket
    }

    fun getSocket(socketIndex: Int): SSLSocket? {
        return SOCKETS[socketIndex % SOCKETS.size]
    }

    fun getLock(id: Int): Int {
        val mid = id % SOCKETS.size
        LOCKS.putIfAbsent(mid, mid)
        return LOCKS[mid] ?: 0
    }
}