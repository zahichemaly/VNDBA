package com.booboot.vndbandroid.api

import android.util.Log
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.model.vndb.*
import com.booboot.vndbandroid.util.ErrorHandler
import com.booboot.vndbandroid.util.PreferencesManager
import com.booboot.vndbandroid.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLException
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class VNDBServer @Inject constructor(
        private val gson: Gson,
        private val schedulers: Schedulers
) {
    private fun <T> connect(socketIndex: Int, emitter: SingleEmitter<Response<T>>): Boolean {
        try {
            val sf = SSLSocketFactory.getDefault()
            val socket = sf.createSocket(HOST, PORT) as SSLSocket
            socket.keepAlive = false
            socket.soTimeout = 0

            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            val s = socket.session

            if (!hv.verify(HOST, s)) {
                emitter.onError(Throwable("The API's certificate is not valid. Expected " + HOST))
                return false
            }

            SocketPool.setSocket(socketIndex, socket)
            return true
        } catch (uhe: UnknownHostException) {
            Utils.processException(uhe)
            emitter.onError(Throwable("Unable to reach the server $HOST. Please try again later."))
            return false
        } catch (ioe: IOException) {
            Utils.processException(ioe)
            emitter.onError(Throwable("An error occurred during the connection to the server. Please try again later."))
            return false
        }
    }

    fun <T> login(socketIndex: Int, originalEmitter: SingleEmitter<Response<T>>) {
        synchronized(SocketPool.getLock(socketIndex)) {
            if (SocketPool.getSocket(socketIndex) == null) {
                if (!connect(socketIndex, originalEmitter)) return
                val username = PreferencesManager.username()?.toLowerCase()?.trim() ?: ""
                val password = PreferencesManager.password() ?: ""
                Single.create<Response<Void>> { emitter ->
                    sendCommand("login ", Login(PROTOCOL, CLIENT, CLIENTVER, username, password), socketIndex, emitter, object : TypeToken<Void>() {
                    })
                }
                        .doOnError { t: Throwable -> originalEmitter.onError(t) }
                        .subscribe()
            }
        }
    }

    fun <T> get(type: String, flags: String, filters: String, options: Options, resultClass: TypeToken<Results<T>>): Single<Results<T>> {
        val command = StringBuilder().append("get ").append(type).append(' ').append(flags).append(' ').append(filters).append(' ')

        val observables = mutableListOf<Single<Response<Results<T>>>>()
        (0 until options.numberOfPages).mapTo(observables) {
            Single.create<Response<Results<T>>> { emitter ->
                val threadOptions = options.copy()
                threadOptions.page = it + 1
                sendCommand(command.toString(), threadOptions, it, emitter, resultClass)
            }.subscribeOn(schedulers.newThread())
        }

        return Single.merge(observables)
                .collect({ Results<T>() }, { results, response ->
                    results.items.addAll(response.results?.items ?: emptyList())
                })
    }

    fun set(type: String, id: Int, fields: Fields): Completable {
        val command = StringBuilder().append("set ").append(type).append(' ').append(id).append(' ')

        return Single.create<Response<Void>> { emitter ->
            sendCommand(command.toString(), fields, 0, emitter, object : TypeToken<Void>() {
            })
        }.flatMapCompletable { Completable.complete() }
    }

    fun dbstats(): Single<DbStats> {
        return Single.create<Response<DbStats>> { emitter ->
            sendCommand("dbstats", null, 0, emitter, object : TypeToken<DbStats>() {
            })
        }.map { response: Response<DbStats> -> response.results!! }
    }

    private fun <T> sendCommand(command: String, params: Any?, socketIndex: Int, emitter: SingleEmitter<Response<T>>, resultClass: TypeToken<T>) {
        return sendCommand(command, params, socketIndex, emitter, resultClass, false)
    }

    private fun <T> sendCommand(command: String, params: Any?, socketIndex: Int, emitter: SingleEmitter<Response<T>>, resultClass: TypeToken<T>, retry: Boolean) {
        synchronized(SocketPool.getLock(socketIndex)) {
            val query = StringBuilder()
            query.append(command)
            if (params != null)
                query.append(gson.toJson(params))
            query.append(EOM)

            val input: InputStreamReader
            val output: OutputStream
            var response: Response<T>?
            var isThrottled: Boolean
            try {
                val socket = SocketPool.getSocket(this, socketIndex, emitter)
                if (socket == null) {
                    emitter.onError(Throwable("Unable to connect. Please try again later."))
                    return
                }

                output = socket.outputStream
                input = InputStreamReader(socket.inputStream)

                do {
                    if (BuildConfig.DEBUG) {
                        Log.e("D", query.toString())
                    }
                    if (input.ready()) while (input.read() > -1);
                    output.flush()
                    output.write(query.toString().toByteArray(charset("UTF-8")))

                    isThrottled = false
                    response = getResponse(socketIndex, input, emitter, resultClass)
                    if (response != null && response.error != null) {
                        val error = response.error!!
                        isThrottled = error.id == "throttled"
                        if (isThrottled) {
                            if (SocketPool.throttleHandlingSocket < 0) SocketPool.throttleHandlingSocket = socketIndex

                            /* We got throttled by the server. Displaying a warning message */
                            val fullwait = Math.round(error.fullwait / 60)
                            ErrorHandler.showToast(String.format(App.instance.getString(R.string.throttle_warning), fullwait))
                            try {
                                /* Waiting ~minwait if the we are in the thread that handles the throttle, 5+ minwaits otherwise */
                                val waitingFactor = (if (SocketPool.throttleHandlingSocket == socketIndex) 1050 else 5000 + 500 * socketIndex).toLong()
                                Thread.sleep(Math.round(error.minwait * waitingFactor))
                            } catch (e: InterruptedException) {
                                /* For some reason we weren't able to sleep, so we can ignore the exception and keep rolling anyway */
                            }
                        } else {
                            emitter.onError(Throwable(error.fullMessage()))
                            return
                        }
                    }
                } while (isThrottled)
            } catch (uee: UnsupportedEncodingException) {
                Utils.processException(uee)
                VNDBServer.close(socketIndex)
                emitter.onError(Throwable("Tried to send a query to the API with a wrong encoding. Aborting operation."))
                return
            } catch (se: SocketException) {
                Utils.processException(se)
                VNDBServer.close(socketIndex)
                emitter.onError(Throwable("A connection error occurred. Please check your connection or try again later."))
                return
            } catch (ssle: SSLException) {
                VNDBServer.close(socketIndex)
                if (retry) {
                    Utils.processException(ssle)
                    emitter.onError(Throwable("An error occurred while writing a query to the API. Please try again later."))
                    return
                } else {
                    return sendCommand(command, params, socketIndex, emitter, resultClass, true)
                }
            } catch (ioe: IOException) {
                Utils.processException(ioe)
                VNDBServer.close(socketIndex)
                emitter.onError(Throwable("An error occurred while sending a query to the API. Please try again later."))
                return
            } finally {
                if (SocketPool.throttleHandlingSocket > -1 && SocketPool.throttleHandlingSocket == socketIndex)
                    SocketPool.throttleHandlingSocket = -1
            }

            if (response != null) emitter.onSuccess(response)
        }
    }

    private fun <T> getResponse(socketIndex: Int, input: InputStreamReader, emitter: SingleEmitter<Response<T>>, resultClass: TypeToken<T>): Response<T>? {
        val responseWrapper = Response<T>()
        val response = StringBuilder()
        try {
            var read = input.read()
            while (read != 4 && read > -1) {
                response.append(read.toChar())
                read = input.read()
                // Log.e("D", response.toString());
            }
        } catch (exception: Exception) {
            Utils.processException(exception)
            VNDBServer.close(socketIndex)
            emitter.onError(Throwable("An error occurred while receiving the response from the API. Please try again later."))
            return null
        }

        if (BuildConfig.DEBUG) {
            //    log(response.toString());
        }

        val delimiterIndex = response.indexOf("{")
        if (delimiterIndex < 0) {
            return if (response.toString().trim() == "ok") {
                responseWrapper.ok = true
                responseWrapper
            } else {
                /* Undocumented error : the server returned an empty response (""), which means absolutely nothing but "leave the ship because something undebuggable happened!" */
                VNDBServer.close(socketIndex)
                val message = "VNDB.org returned an unexpected error. Please try again later."
                emitter.onError(Throwable(message))
                Utils.processException(Exception(message))
                null
            }
        }

        return try {
            val command = response.substring(0, delimiterIndex).trim()
            val params = response.substring(delimiterIndex, response.length).replace(EOM + "", "")
            if (command == "error") {
                responseWrapper.error = gson.fromJson(params, Error::class.java)
            } else {
                responseWrapper.results = gson.fromJson(params, resultClass.type)
            }
            responseWrapper
        } catch (ioe: IOException) {
            Utils.processException(ioe)
            VNDBServer.close(socketIndex)
            emitter.onError(Throwable("An error occurred while decoding the response from the API. Aborting operation."))
            null
        }
    }

    companion object {
        private val HOST = "api.vndb.org"
        private val PORT = 19535
        private val EOM: Char = 0x04.toChar()

        private val PROTOCOL = 1
        private val CLIENT = "VNDB_ANDROID"
        private val CLIENTVER = 3.0

        fun close(socketIndex: Int) {
            try {
                SocketPool.getSocket(socketIndex)?.let {
                    it.inputStream.close()
                    it.outputStream.close()
                    it.close()
                    SocketPool.setSocket(socketIndex, null)
                }
            } catch (ioe: IOException) {
                Utils.processException(ioe)
            }
        }

        fun closeAll() {
            Completable.create {
                try {
                    for (i in 0 until SocketPool.MAX_SOCKETS) {
                        SocketPool.getSocket(i)?.let {
                            it.inputStream.close()
                            it.outputStream.close()
                            it.close()
                            SocketPool.setSocket(i, null)
                        }
                    }
                } catch (ioe: IOException) {
                    Utils.processException(ioe)
                }
            }
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .subscribe()
        }

        @Suppress("unused")
        fun log(sb: String) {
            if (sb.length > 4000) {
                val chunkCount = sb.length / 4000     // integer division
                for (i in 0..chunkCount) {
                    val max = 4000 * (i + 1)
                    if (max >= sb.length) {
                        Log.e("D", sb.substring(4000 * i))
                    } else {
                        Log.e("D", sb.substring(4000 * i, max))
                    }
                }
            } else {
                Log.e("D", sb)
            }
        }
    }
}