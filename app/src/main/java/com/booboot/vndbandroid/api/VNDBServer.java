package com.booboot.vndbandroid.api;

import android.content.Context;
import android.util.Log;

import com.booboot.vndbandroid.api.bean.*;
import com.booboot.vndbandroid.api.bean.Error;
import com.booboot.vndbandroid.json.JSON;
import com.booboot.vndbandroid.settings.SettingsManager;
import com.booboot.vndbandroid.util.Callback;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
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
                } else if (response instanceof Error) {
                    if (((Error) response).getId().equals("auth")) {
                        errorCallback.message = "Incorrect username/password combination. Please verify your login information, or create an account if you don't have one.";
                        errorCallback.call();
                    } else if (((Error) response).getId().equals("loggedin")) {
                        errorCallback.message = "You are already logged in.";
                        errorCallback.call();
                    }
                }
            }
        }.start();
    }

    public static void get(final String type, final String flags, final String filters, final Options options, final Context context, final Callback successCallback, final Callback errorCallback) {
        while (!mutex)
            try { Thread.sleep(50); } catch (InterruptedException ie) { return; }
        mutex = false;

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
                VNDBCommand results = sendCommand(command.toString(), options);
                if (results instanceof Error) {
                    errorCallback.message = ((Error) results).getMsg();
                    errorCallback.call();
                } else if (results instanceof Results) {
                    successCallback.results = (Results) results;
                    successCallback.call();
                }

                mutex = true;
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
            out.write(query.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            errorCallback.message = "Tried to send a query to the API with a wrong encoding. Aborting operation.";
            errorCallback.call();
            return null;
        } catch (IOException ioe) {
            errorCallback.message = "An error occurred while sending a query to the API. Please try again later.";
            errorCallback.call();
            return null;
        }
        return getResponse();
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
        Log.e("D", "Server: " + response.toString());

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
}
