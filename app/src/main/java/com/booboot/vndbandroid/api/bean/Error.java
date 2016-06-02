package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error extends VNDBCommand {
    private String id;
    private String msg;

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

    public String getFullMessage() {
        switch (id) {
            case "throttled":
                return "VNDB.org is too busy to fulfill your request now, so your lists may not be up-to-date. Please wait a bit and try again.";
            default:
                return id + " : " + msg;
        }
    }
}
