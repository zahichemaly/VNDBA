package com.booboot.vndbandroid.bean;

import android.content.Context;

import com.booboot.vndbandroid.util.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mail extends VNDBCommand {
    private static Mail INFO;
    private String username;
    private String password;
    private String to;

    public static Mail getInfo(Context context) {
        if (INFO != null) return INFO;

        INFO = new Mail();
        InputStream ins = context.getResources().openRawResource(context.getResources().getIdentifier("mail", "raw", context.getPackageName()));
        try {
            INFO = JSON.mapper.readValue(ins, Mail.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return INFO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
