package com.booboot.vndbandroid.bean.vnstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNStatResults {
    private boolean success;
    private long nextUpdate;
    private VNStatItem result;
    private String message;

    public VNStatItem getResult() {
        return result;
    }

    public void setResult(VNStatItem result) {
        this.result = result;
    }

    public long getNextUpdate() {
        return nextUpdate;
    }

    public void setNextUpdate(int nextUpdate) {
        this.nextUpdate = nextUpdate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
