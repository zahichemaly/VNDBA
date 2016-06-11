package com.booboot.vndbandroid.api;

import android.content.Context;
import android.util.Log;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.DbStats;
import com.booboot.vndbandroid.api.bean.Error;
import com.booboot.vndbandroid.api.bean.Fields;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Login;
import com.booboot.vndbandroid.api.bean.Ok;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.api.bean.Results;
import com.booboot.vndbandroid.api.bean.VNDBCommand;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.NumberedThread;
import com.booboot.vndbandroid.util.SettingsManager;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by od on 12/03/2016.
 */
public class VNDBServer {
    public final static String HOST = "api.vndb.org";
    public final static int PORT = 19535;
    public final static char EOM = 0x04;

    public final static int PROTOCOL = 1;
    public final static String CLIENT = "VNDB_ANDROID";
    public final static double CLIENTVER = 1.1;

    private static boolean mutex = true;
    private static Context context;
    private static Callback successCallback, errorCallback;
    private static boolean useCacheIfError;

    /* Pipelining variables */
    private static ExecutorService threadManager;
    private static VNDBCommand[] plPageResults;

    private static void init(Context c, Callback sc, Callback ec, boolean ucie) {
        context = c;
        successCallback = sc;
        errorCallback = ec;
        useCacheIfError = ucie;
    }

    public static boolean connect(int socketIndex) {
        try {
            SocketFactory sf = SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket(HOST, PORT);
            socket.setKeepAlive(true);
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
            errorCallback.message = "Unable to reach the server " + HOST + ". Please try again later.";
            errorCallback.call();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            errorCallback.message = "An error occurred during the connection to the server. Please try again later.";
            errorCallback.call();
            return false;
        }
    }

    public static void login(final Context context, final int socketIndex, final Callback successCallback, final Callback errorCallback) {
        synchronized (SocketPool.getLock(socketIndex)) {
            if (SocketPool.getSocket(socketIndex) == null) {
                if (!connect(socketIndex)) return;
                String username = SettingsManager.getUsername(context).toLowerCase().trim();
                String password = SettingsManager.getPassword(context);
                VNDBCommand response = sendCommand("login ", Login.create(PROTOCOL, CLIENT, CLIENTVER, username, password), socketIndex);
                if (!(response instanceof Ok)) {
                    errorCallback.call();
                }
            }
        }
    }

    public static void get(final String type, final String flags, final String filters, final Options options, final int socketIndex, final Context context, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                init(context, successCallback, errorCallback, options.isUseCacheIfError());
                final StringBuilder command = new StringBuilder();
                command.append("get ");
                command.append(type);
                command.append(' ');
                command.append(flags);
                command.append(' ');
                command.append(filters);
                command.append(' ');

                VNDBCommand results = null;
                if (options != null && options.getNumberOfPages() > 1) {
                    plPageResults = new VNDBCommand[options.getNumberOfPages()];
                    threadManager = Executors.newCachedThreadPool();
                    for (int i = 0; i < options.getNumberOfPages(); i++) {
                        threadManager.execute(new NumberedThread(i) {
                            public void run() {
                                synchronized (SocketPool.getLock(num)) {
                                    Options threadOptions = Options.create(options);
                                    threadOptions.setPage(num + 1);
                                    plPageResults[num] = sendCommand(command.toString(), threadOptions, num);
                                }
                            }
                        });
                    }

                    threadManager.shutdown();
                    try {
                        threadManager.awaitTermination(30, TimeUnit.MINUTES);
                        threadManager = null;
                        for (int i = 0; i < plPageResults.length; i++) {
                            if (results == null) {
                                results = plPageResults[i];
                            } else if (results instanceof Results && plPageResults[i] instanceof Results) {
                                ((Results) results).getItems().addAll(((Results) plPageResults[i]).getItems());
                            }

                            if (plPageResults == null) continue;
                        }

                        if (results instanceof Results && plPageResults != null) {
                            successCallback.results = (Results) results;
                            successCallback.call();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    synchronized (SocketPool.getLock(socketIndex)) {
                        VNDBCommand pageResults;
                        do {
                            pageResults = sendCommand(command.toString(), options, socketIndex);
                            if (results == null) {
                                results = pageResults;
                            } else if (results instanceof Results && pageResults instanceof Results) {
                                /* If there there's more than 1 page, we add the current page items to the overall results, to avoid overwriting the results of the previous pages */
                                ((Results) results).getItems().addAll(((Results) pageResults).getItems());
                            }

                            if (options == null || pageResults == null) break;
                            options.setPage(options.getPage() + 1);
                        } while (options.isFetchAllPages() && pageResults instanceof Results && ((Results) pageResults).isMore());

                        if (results instanceof Results && pageResults != null) {
                            successCallback.results = (Results) results;
                            successCallback.call();
                        }
                    }
                }
            }
        }.start();
    }

    public static void set(final String type, final int id, final Fields fields, final Context context, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                if (!takeMutex()) return;
                init(context, successCallback, errorCallback, false);
                StringBuilder command = new StringBuilder();
                command.append("set ");
                command.append(type);
                command.append(' ');
                command.append(id);
                command.append(' ');
                VNDBCommand results = sendCommand(command.toString(), fields, 0);
                if (results instanceof Ok) {
                    if (successCallback != null) successCallback.call();
                }

                freeMutex();
            }
        }.start();
    }

    public static void dbstats(final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                if (!takeMutex()) return;
                init(context, successCallback, errorCallback, false);
                VNDBCommand results = sendCommand("dbstats", null, 0);
                if (results instanceof DbStats) {
                    successCallback.dbstats = (DbStats) results;
                    successCallback.call();
                } else {
                    Cache.loadStatsFromCache(context);
                    successCallback.dbstats = Cache.dbstats;
                    successCallback.call();
                }

                freeMutex();
            }
        }.start();
    }

    public static VNDBCommand sendCommand(String command, VNDBCommand params, int socketIndex) {
        StringBuilder query = new StringBuilder();
        try {
            query.append(command);
            if (params != null)
                query.append(JSON.mapper.writeValueAsString(params));
            query.append(EOM);
        } catch (JsonProcessingException jpe) {
            errorCallback.message = "Unable to process the query to the API as JSON. Aborting operation.";
            errorCallback.call();
            return null;
        }

        InputStreamReader in;
        OutputStream out;
        VNDBCommand response;
        boolean isThrottled;
        try {
            SSLSocket socket = SocketPool.getSocket(context, socketIndex, successCallback, errorCallback);
            if (socket == null) return null;
            in = new InputStreamReader(socket.getInputStream());
            out = socket.getOutputStream();

            do {
                if (BuildConfig.DEBUG) {
                    Log.e("D", query.toString());
                }
                if (in.ready()) while (in.read() > -1) ;
                out.flush();
                out.write(query.toString().getBytes("UTF-8"));

                isThrottled = false;
                response = getResponse(in);
                if (response instanceof Error) {
                    Error error = (Error) response;
                    isThrottled = error.getId().equals("throttled");
                    if (isThrottled) {
                        /* We got throttled by the server. Displaying a warning message */
                        long fullwait = Math.round(error.getFullwait() / 60);
                        Callback.showToast(context, String.format(context.getString(R.string.throttle_warning), fullwait));
                        try {
                            /* Waiting minwait*3 seconds before trying this query again */
                            Thread.sleep(Math.round(error.getMinwait() * 3000));
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
            errorCallback.message = "Tried to send a query to the API with a wrong encoding. Aborting operation.";
            errorCallback.call();
            return null;
        } catch (SocketException se) {
            se.printStackTrace();
            VNDBServer.close(socketIndex);

            if (!useCacheIfError || !Cache.loadFromCache(context)) {
                errorCallback.message = ConnectionReceiver.CONNECTION_ERROR_MESSAGE;
                errorCallback.call();
            } else {
                Results results = new Results();
                results.setItems(new ArrayList<Item>());
                successCallback.results = results;
                successCallback.call();
            }
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            errorCallback.message = "An error occurred while sending a query to the API. Please try again later.";
            errorCallback.call();
            return null;
        }

        return response;
    }

    private static VNDBCommand getResponse(InputStreamReader in) {
        StringBuilder response = new StringBuilder();
        try {
            int read = in.read();
            while (read != 4 && read > -1) {
                response.append((char) read);
                read = in.read();
                // Log.e("D", response.toString());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            errorCallback.message = "An error occurred while receiving the response from the API. Please try again later.";
            errorCallback.call();
            return null;
        }
        if (BuildConfig.DEBUG) {
            log(response.toString());
        }

        int delimiterIndex = response.indexOf("{");
        if (delimiterIndex < 0) {
            if (response.toString().trim().equals("ok"))
                return new Ok();
            else if (threadManager == null || !threadManager.isShutdown()) {
                /* Calling the error callback only if we're not pipelining (might be wrong to call the error callback while there are other threads running) */
                errorCallback.message = "An error occurred while reading the response from the API. Aborting operation.";
                errorCallback.call();
                return null;
            }
        }

        try {
            String command = response.substring(0, delimiterIndex).trim();
            String params = response.substring(delimiterIndex, response.length()).replace(EOM + "", "");
            return (VNDBCommand) JSON.mapper.readValue(params, VNDBCommand.getClass(command));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            errorCallback.message = "An error occurred while decoding the response from the API. Aborting operation.";
            errorCallback.call();
            return null;
        }
    }

    public static void close(final int socketIndex) {
        new Thread() {
            public void run() {
                try {
                    SSLSocket socket = SocketPool.getSocket(socketIndex);
                    if (socket != null && !socket.isClosed()) {
                        socket.getInputStream().close();
                        socket.getOutputStream().close();
                        socket.close();
                        SocketPool.setSocket(socketIndex, null);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();
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
                            SocketPool.setSocket(i, null);
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();
    }

    private static boolean takeMutex() {
        while (!mutex)
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
                return false;
            }
        mutex = false;
        return true;
    }

    private static void freeMutex() {
        mutex = true;
    }

    private static void log(String sb) {
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
