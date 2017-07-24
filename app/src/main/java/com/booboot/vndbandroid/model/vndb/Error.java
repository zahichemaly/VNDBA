package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private String id;
    private String msg;
    private String type;
    private double minwait;
    private double fullwait;

    public Error() {
    }

    public Error(String id, int minwait, int fullwait) {
        this.id = id;
        this.minwait = minwait;
        this.fullwait = fullwait;
    }

    public String getFullMessage() {
        switch (id) {
            case "throttled":
                return "VNDB.org is too busy to fulfill your request now, so your lists may not be up-to-date. Please wait a bit and try again.";
            default:
                return id + " : " + msg;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMinwait() {
        return minwait;
    }

    public void setMinwait(double minwait) {
        this.minwait = minwait;
    }

    public double getFullwait() {
        return fullwait;
    }

    public void setFullwait(double fullwait) {
        this.fullwait = fullwait;
    }

}
