package com.booboot.vndbandroid.api;

import android.content.Context;

import com.booboot.vndbandroid.util.Callback;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.net.ssl.SSLSocket;

/**
 * Created by od on 04/06/2016.
 */
public class SocketPool {
    public final static int MAX_SOCKETS = 5;
    public final static SSLSocket[] SOCKETS = new SSLSocket[MAX_SOCKETS];
    public final static ConcurrentMap<Integer, Integer> LOCKS = new ConcurrentHashMap<>();

    public static SSLSocket getSocket(Context context, int socketIndex, Callback errorCallback) {
        socketIndex %= SOCKETS.length;
        VNDBServer.login(context, socketIndex, errorCallback);
        return SOCKETS[socketIndex];
    }

    public static void setSocket(int socketIndex, SSLSocket socket) {
        SOCKETS[socketIndex % SOCKETS.length] = socket;
    }

    public static SSLSocket getSocket(int socketIndex) {
        return SOCKETS[socketIndex % SOCKETS.length];
    }

    public static Object getLock(Integer id) {
        id %= SOCKETS.length;
        LOCKS.putIfAbsent(id, id);
        return LOCKS.get(id);
    }
}
