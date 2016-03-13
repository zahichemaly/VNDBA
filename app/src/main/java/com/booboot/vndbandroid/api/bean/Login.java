package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login extends VNDBCommand {
    private int protocol;
    private String client;
    private double clientver;
    private String username;
    private String password;

    public static Login create(int protocol, String client, double clientver, String username, String password) {
        Login login = new Login();
        login.protocol = protocol;
        login.client = client;
        login.clientver = clientver;
        login.username = username;
        login.password = password;
        return login;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public double getClientver() {
        return clientver;
    }

    public void setClientver(double clientver) {
        this.clientver = clientver;
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
}
