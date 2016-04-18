package com.booboot.vndbandroid.api;

import android.content.Context;
import android.system.ErrnoException;
import android.util.Log;

import com.booboot.vndbandroid.api.bean.DbStats;
import com.booboot.vndbandroid.api.bean.Error;
import com.booboot.vndbandroid.api.bean.Fields;
import com.booboot.vndbandroid.api.bean.Login;
import com.booboot.vndbandroid.api.bean.Ok;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.api.bean.Results;
import com.booboot.vndbandroid.api.bean.VNDBCommand;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.SettingsManager;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by od on 12/03/2016.
 */
public class VNDBServer {
    public final static String HOST = "api.vndb.org";
    public final static int PORT = 19534;
    public final static char EOM = 0x04;

    public final static int PROTOCOL = 1;
    public final static String CLIENT = "VNDB_ANDROID";
    public final static double CLIENTVER = 0.1;

    private static boolean mutex = true;
    private static Socket socket;
    private static OutputStream out;
    private static InputStreamReader in;
    private static Context context;
    private static Callback successCallback, errorCallback;

    private static void init(Context c, Callback sc, Callback ec) {
        context = c;
        successCallback = sc;
        errorCallback = ec;
    }

    private static boolean connect() {
        try {
            socket = new Socket(HOST, PORT);
            socket.setKeepAlive(true);
            out = socket.getOutputStream();
            in = new InputStreamReader(socket.getInputStream());
            return true;
        } catch (UnknownHostException uhe) {
            errorCallback.message = "Unable to reach the server " + HOST + ". Please try again later.";
            errorCallback.call();
            return false;
        } catch (IOException ioe) {
            errorCallback.message = "An error occurred during the connection to the server. Please try again later.";
            errorCallback.call();
            return false;
        }
    }

    public static void login(final Context context, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                init(context, successCallback, errorCallback);
                if (!connect()) return;
                /* [IMPORTANT] Always put this character at the end of a message */
                String username = SettingsManager.getUsername(context).toLowerCase();
                String password = SettingsManager.getPassword(context);
                /* [IMPORTANT] Remember to always put the username in lowercase here */
                VNDBCommand response = sendCommand("login", Login.create(PROTOCOL, CLIENT, CLIENTVER, username, password));
                if (response instanceof Ok) {
                    successCallback.call();
                }
            }
        }.start();
    }

    public static void get(final String type, final String flags, final String filters, final Options options, final boolean fetchAllPages, final Context context, final Callback successCallback, final Callback errorCallback) {
        if (!takeMutex()) return;

        new Thread() {
            public void run() {
                init(context, successCallback, errorCallback);
                StringBuilder command = new StringBuilder();
                command.append("get ");
                command.append(type);
                command.append(' ');
                command.append(flags);
                command.append(' ');
                command.append(filters);
                command.append(' ');
                VNDBCommand results;
                do {
                    results = sendCommand(command.toString(), options);
                    if (options == null) break;
                    options.setPage(options.getPage() + 1);
                } while (fetchAllPages && results instanceof Results && ((Results) results).isMore());

                if (results instanceof Results) {
                    successCallback.results = (Results) results;
                    successCallback.call();
                }

                freeMutex();
            }
        }.start();
    }

    public static void set(final String type, final int id, final Fields fields, final Context context, final Callback successCallback, final Callback errorCallback) {
        if (!takeMutex()) return;

        new Thread() {
            public void run() {
                init(context, successCallback, errorCallback);
                StringBuilder command = new StringBuilder();
                command.append("set ");
                command.append(type);
                command.append(' ');
                command.append(id);
                command.append(' ');
                VNDBCommand results = sendCommand(command.toString(), fields);
                if (results instanceof Ok) {
                    if (successCallback != null) successCallback.call();
                }

                freeMutex();
            }
        }.start();
    }

    public static void dbstats(final Callback successCallback, final Callback errorCallback) {
        if (!takeMutex()) return;

        new Thread() {
            public void run() {
                VNDBCommand results = sendCommand("dbstats", null);
                if (results instanceof DbStats) {
                    successCallback.dbstats = (DbStats) results;
                    successCallback.call();
                }

                freeMutex();
            }
        }.start();
    }

    public static VNDBCommand sendCommand(String command, VNDBCommand params) {
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

        try {
            Log.e("D", query.toString());
            if (in.ready()) while (in.read() > -1) ;
            out.write(query.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            errorCallback.message = "Tried to send a query to the API with a wrong encoding. Aborting operation.";
            errorCallback.call();
            return null;
        } catch (SocketException se) {
            errorCallback.message = "You don't seem to have an Internet connection. Please check your connection or try again later.";
            errorCallback.call();
            return null;
        }catch (IOException ioe) {
            errorCallback.message = "An error occurred while sending a query to the API. Please try again later.";
            errorCallback.call();
            return null;
        }

        VNDBCommand response = getResponse();
        if (response instanceof Error) {
            errorCallback.message = ((Error) response).getId() + " : " + ((Error) response).getMsg();
            errorCallback.call();
        }

        return response;
    }

    private static VNDBCommand getResponse() {
        StringBuilder response = new StringBuilder();
        try {
            int read = in.read();
            while (read != 4 && read > -1) {
                response.append((char) read);
                read = in.read();
                // Log.e("D", response.toString());
            }
        } catch (IOException ioe) {
            errorCallback.message = "An error occurred while receiving the response from the API. Please try again later.";
            errorCallback.call();
            return null;
        }
        log(response.toString());

        int delimiterIndex = response.indexOf("{");
        if (delimiterIndex < 0) {
            if (response.toString().trim().equals("ok"))
                return new Ok();
            else {
                errorCallback.message = "An error occurred while decoding the response from the API. Aborting operation.";
                errorCallback.call();
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

    public static void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ioe) {
        }
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
