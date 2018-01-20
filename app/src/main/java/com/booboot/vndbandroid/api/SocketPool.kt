package com.booboot.vndbandroid.api

import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Response
import io.reactivex.SingleEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

import javax.net.ssl.SSLSocket

object SocketPool {
    const val MAX_SOCKETS = 10
    private val SOCKETS = arrayOfNulls<SSLSocket>(MAX_SOCKETS)
    private val LOCKS: ConcurrentMap<Int, Int> = ConcurrentHashMap()
    var throttleHandlingSocket = -1

    fun <T> getSocket(server: VNDBServer, options: Options, emitter: SingleEmitter<Response<T>>): SSLSocket? {
        server.login(options, emitter)
        return SOCKETS[options.socketIndex]
    }

    fun setSocket(socketIndex: Int, socket: SSLSocket?) {
        SOCKETS[socketIndex] = socket
    }

    fun getSocket(socketIndex: Int): SSLSocket? {
        return SOCKETS[socketIndex]
    }

    fun getLock(id: Int): Int {
        LOCKS.putIfAbsent(id, id)
        return LOCKS[id] ?: 0
    }
}