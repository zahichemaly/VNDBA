package com.booboot.vndbandroid.api;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.model.vndb.DbStats;
import com.booboot.vndbandroid.model.vndb.Error;
import com.booboot.vndbandroid.model.vndb.Fields;
import com.booboot.vndbandroid.model.vndb.Login;
import com.booboot.vndbandroid.model.vndb.Options;
import com.booboot.vndbandroid.model.vndb.Results;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.NumberedThread;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by od on 12/03/2016.
 */
public class VNDBServer {
    public final static String HOST = "api.vndb.org";
    private final static int PORT = 19535;
    private final static char EOM = 0x04;

    private final static int PROTOCOL = 1;
    private final static String CLIENT = "VNDB_ANDROID";
    private final static double CLIENTVER = 3.0;

    /* Pipelining variables */
    private static ExecutorService threadManager;
    private static Response[] plPageResults;

    private static boolean connect(int socketIndex, Callback errorCallback) {
        try {
            SocketFactory sf = SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket(HOST, PORT);
            socket.setKeepAlive(false);
            socket.setSoTimeout(0);

            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            SSLSession s = socket.getSession();

            if (!hv.verify(HOST, s)) {
                errorCallback.message = "The API's certificate is not valid. Expected " + HOST + ", found " + s.getPeerPrincipal().getName();
                errorCallback.call();
                return false;
            }

            SocketPool.setSocket(socketIndex, socket);
            return true;
        } catch (UnknownHostException uhe) {
            Utils.processException(uhe);
            errorCallback.message = "Unable to reach the server " + HOST + ". Please try again later.";
            errorCallback.call();
            return false;
        } catch (IOException ioe) {
            Utils.processException(ioe);
            errorCallback.message = "An error occurred during the connection to the server. Please try again later.";
            errorCallback.call();
            return false;
        }
    }

    public static void login(final Context context, final int socketIndex, final Callback errorCallback) {
        if (context == null) return;
        synchronized (SocketPool.getLock(socketIndex)) {
            if (SocketPool.getSocket(socketIndex) == null) {
                if (!connect(socketIndex, errorCallback)) return;
                String username = SettingsManager.getUsername(context).toLowerCase().trim();
                String password = SettingsManager.getPassword(context);
                Response<Void> response = sendCommand("login ", Login.create(PROTOCOL, CLIENT, CLIENTVER, username, password), socketIndex, context, errorCallback, new TypeReference<Void>() {
                });
                if (response == null || !response.ok) {
                    // Closing the socket to avoid subsequent login errors
                    close(socketIndex);
                    errorCallback.call();
                }
            }
        }
    }

    public static <T> void get(final String type, final String flags, final String filters, final Options options, final int socketIndex, final Context context, final TypeReference<T> resultClass, final Callback<T> successCallback, final Callback errorCallback) {
        new Thread() {
            @SuppressWarnings("unchecked")
            public void run() {
                final StringBuilder command = new StringBuilder();
                command.append("get ");
                command.append(type);
                command.append(' ');
                command.append(flags);
                command.append(' ');
                command.append(filters);
                command.append(' ');

                Response<T> results = null;
                if (options != null && options.getNumberOfPages() > 1) {
                    plPageResults = new Response[options.getNumberOfPages()];
                    threadManager = Executors.newCachedThreadPool();

                    for (int i = 0; i < options.getNumberOfPages(); i++) {
                        threadManager.execute(new NumberedThread(i) {
                            public void run() {
                                Options threadOptions = Options.create(options);
                                threadOptions.setPage(num + 1);
                                plPageResults[num] = sendCommand(command.toString(), threadOptions, num, context, errorCallback, resultClass);
                            }
                        });
                    }

                    threadManager.shutdown();
                    try {
                        threadManager.awaitTermination(30, TimeUnit.MINUTES);
                        threadManager = null;
                        for (Response plPageResult : plPageResults) {
                            /* Error case: plPageResult is null, which means there was an exception in one thread and the results are incomplete.
                            To avoid data corruption and/or bad behavior, we stop immediately: the error callback has already been called internally */
                            if (plPageResult == null || plPageResult.error != null) return;

                            if (results == null) {
                                results = plPageResult;
                            } else if (results.results instanceof Results && plPageResult.results instanceof Results) {
                                ((Results) results.results).getItems().addAll(((Results) plPageResult.results).getItems());
                            }
                        }

                        if (results != null && results.results instanceof Results) {
                            successCallback.results = results.results;
                            successCallback.call();
                        }
                    } catch (InterruptedException e) {
                        Utils.processException(e);
                    }
                } else {
                    Response<T> pageResults;
                    do {
                        pageResults = sendCommand(command.toString(), options, socketIndex, context, errorCallback, resultClass);
                        if (pageResults == null || pageResults.error != null) return;

                        if (results == null) {
                            results = pageResults;
                        } else if (results.results instanceof Results && pageResults.results instanceof Results) {
                            /* If there's more than 1 page, we add the current page items to the overall results, to avoid overwriting the results of the previous pages */
                            ((Results) results.results).getItems().addAll(((Results) pageResults.results).getItems());
                        }

                        if (options == null) break;
                        options.setPage(options.getPage() + 1);
                    } while (options.isFetchAllPages() && pageResults.results instanceof Results && ((Results) pageResults.results).isMore());

                    if (results != null && results.results instanceof Results) {
                        successCallback.results = results.results;
                        successCallback.call();
                    }
                }
            }
        }.start();
    }

    public static void set(final String type, final int id, final Fields fields, final Context context, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                StringBuilder command = new StringBuilder();
                command.append("set ");
                command.append(type);
                command.append(' ');
                command.append(id);
                command.append(' ');
                Response<Void> response = sendCommand(command.toString(), fields, 0, context, errorCallback, new TypeReference<Void>() {
                });
                if (response != null && response.ok) {
                    if (successCallback != null) successCallback.call();
                }
            }
        }.start();
    }

    public static void dbstats(final Context context, final Callback<DbStats> successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                Response<DbStats> response = sendCommand("dbstats", null, 0, context, errorCallback, new TypeReference<DbStats>() {
                });
                if (response != null && response.error == null) {
                    successCallback.results = response.results;
                    successCallback.call();
                } else {
                    errorCallback.call();
                }
            }
        }.start();
    }

    @Nullable
    private static <T> Response<T> sendCommand(String command, Object params, int socketIndex, Context context, Callback errorCallback, TypeReference<T> resultClass) {
        return sendCommand(command, params, socketIndex, context, errorCallback, resultClass, false);
    }

    @Nullable
    private static <T> Response<T> sendCommand(String command, Object params, int socketIndex, Context context, Callback errorCallback, TypeReference<T> resultClass, boolean retry) {
        synchronized (SocketPool.getLock(socketIndex)) {
            StringBuilder query = new StringBuilder();
            try {
                query.append(command);
                if (params != null)
                    query.append(JSON.mapper.writeValueAsString(params));
                query.append(EOM);
            } catch (JsonProcessingException jpe) {
                Utils.processException(jpe);
                VNDBServer.close(socketIndex);
                errorCallback.message = "Unable to process the query to the API as JSON. Aborting operation.";
                errorCallback.call();
                return null;
            }

            InputStreamReader in;
            OutputStream out;
            Response<T> response;
            boolean isThrottled;
            try {
                SSLSocket socket = SocketPool.getSocket(context, socketIndex, errorCallback);
                if (socket == null) return null;
                out = socket.getOutputStream();
                in = new InputStreamReader(socket.getInputStream());

                do {
                    if (BuildConfig.DEBUG) {
                        Log.e("D", query.toString());
                    }
                    if (in.ready()) while (in.read() > -1) ;
                    out.flush();
                    out.write(query.toString().getBytes("UTF-8"));

                    isThrottled = false;
                    response = getResponse(socketIndex, in, errorCallback, resultClass);
                    if (response != null && response.error != null) {
                        Error error = response.error;
                        isThrottled = error.getId().equals("throttled");
                        if (isThrottled) {
                            if (SocketPool.throttleHandlingSocket < 0) SocketPool.throttleHandlingSocket = socketIndex;

                            /* We got throttled by the server. Displaying a warning message */
                            long fullwait = Math.round(error.getFullwait() / 60);
                            Callback.showToast(context, String.format(context.getString(R.string.throttle_warning), fullwait));
                            try {
                                /* Waiting ~minwait if the we are in the thread that handles the throttle, 5+ minwaits otherwise */
                                long waitingFactor = SocketPool.throttleHandlingSocket == socketIndex ? 1050 : 5000 + 500 * socketIndex;
                                Thread.sleep(Math.round(error.getMinwait() * waitingFactor));
                            } catch (InterruptedException e) {
                                /* For some reason we weren't able to sleep, so we can ignore the exception and keep rolling anyway */
                            }
                        } else {
                            errorCallback.message = error.getFullMessage();
                            errorCallback.call();
                        }
                    }
                } while (isThrottled);
            } catch (UnsupportedEncodingException uee) {
                Utils.processException(uee);
                VNDBServer.close(socketIndex);
                errorCallback.message = "Tried to send a query to the API with a wrong encoding. Aborting operation.";
                errorCallback.call();
                return null;
            } catch (SocketException se) {
                Utils.processException(se);
                VNDBServer.close(socketIndex);
                errorCallback.message = ConnectionReceiver.CONNECTION_ERROR_MESSAGE;
                errorCallback.call();
                return null;
            } catch (SSLException ssle) {
                VNDBServer.close(socketIndex);
                if (retry) {
                    Utils.processException(ssle);
                    errorCallback.message = "An error occurred while writing a query to the API. Please try again later.";
                    errorCallback.call();
                    return null;
                } else {
                    return sendCommand(command, params, socketIndex, context, errorCallback, resultClass, true);
                }
            } catch (IOException ioe) {
                Utils.processException(ioe);
                VNDBServer.close(socketIndex);
                errorCallback.message = "An error occurred while sending a query to the API. Please try again later.";
                errorCallback.call();
                return null;
            } finally {
                if (SocketPool.throttleHandlingSocket > -1 && SocketPool.throttleHandlingSocket == socketIndex)
                    SocketPool.throttleHandlingSocket = -1;
            }

            return response;
        }
    }

    private final static class Response<T> {
        Error error;
        T results;
        boolean ok;
    }

    @Nullable
    private static <T> Response<T> getResponse(int socketIndex, InputStreamReader in, Callback errorCallback, TypeReference<T> resultClass) {
        Response<T> responseWrapper = new Response<>();
        StringBuilder response = new StringBuilder();
        try {
            int read = in.read();
            while (read != 4 && read > -1) {
                response.append((char) read);
                read = in.read();
                // Log.e("D", response.toString());
            }
        } catch (Exception exception) {
            Utils.processException(exception);
            VNDBServer.close(socketIndex);
            errorCallback.message = "An error occurred while receiving the response from the API. Please try again later.";
            errorCallback.call();
            return null;
        }
        if (BuildConfig.DEBUG) {
            //    log(response.toString());
        }

        int delimiterIndex = response.indexOf("{");
        if (delimiterIndex < 0) {
            if (response.toString().trim().equals("ok")) {
                responseWrapper.ok = true;
                return responseWrapper;
            } else {
                /* Undocumented error : the server returned an empty response (""), which means absolutely nothing but "leave the ship because something undebuggable happened!" */
                VNDBServer.close(socketIndex);
                errorCallback.message = "VNDB.org returned an unexpected error. Please try again later.";
                Utils.processException(new Exception(errorCallback.message));
                errorCallback.call();
                return null;
            }
        }

        try {
            String command = response.substring(0, delimiterIndex).trim();
            String params = response.substring(delimiterIndex, response.length()).replace(EOM + "", "");
            if (command.equals("error")) {
                responseWrapper.error = JSON.mapper.readValue(params, Error.class);
            } else {
                responseWrapper.results = JSON.mapper.readValue(params, resultClass);
            }
            return responseWrapper;
        } catch (IOException ioe) {
            Utils.processException(ioe);
            VNDBServer.close(socketIndex);
            errorCallback.message = "An error occurred while decoding the response from the API. Aborting operation.";
            errorCallback.call();
            return null;
        }
    }

    public static void close(final int socketIndex) {
        try {
            SSLSocket socket = SocketPool.getSocket(socketIndex);
            if (socket != null && !socket.isClosed()) {
                socket.getInputStream().close();
                socket.getOutputStream().close();
                socket.close();
            }
            SocketPool.setSocket(socketIndex, null);
        } catch (IOException ioe) {
            Utils.processException(ioe);
        }
    }

    public static void closeAll() {
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < SocketPool.MAX_SOCKETS; i++) {
                        SSLSocket socket = SocketPool.getSocket(i);
                        if (socket != null && !socket.isClosed()) {
                            socket.getInputStream().close();
                            socket.getOutputStream().close();
                            socket.close();
                        }
                        SocketPool.setSocket(i, null);
                    }
                } catch (IOException ioe) {
                    Utils.processException(ioe);
                }
            }
        }.start();
    }

    public static void log(String sb) {
        if (sb.length() > 4000) {
            int chunkCount = sb.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= sb.length()) {
                    Log.e("D", sb.substring(4000 * i));
                } else {
                    Log.e("D", sb.substring(4000 * i, max));
                }
            }
        } else {
            Log.e("D", sb);
        }
    }
}
