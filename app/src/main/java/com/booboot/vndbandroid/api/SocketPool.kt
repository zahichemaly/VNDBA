package com.booboot.vndbandroid.api

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

    fun <T> getSocket(server: VNDBServer, socketIndex: Int, emitter: SingleEmitter<Response<T>>): SSLSocket? {
        val indexedSocketIndex = socketIndex % SOCKETS.size
        server.login(indexedSocketIndex, emitter)
        return SOCKETS[indexedSocketIndex]
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