package com.booboot.vndbandroid.model.vndbandroid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNlistItem extends CacheItem {
    protected int status;
    protected String notes;

    public VNlistItem() {
    }

    public VNlistItem(int vn, int added, int status, String notes) {
        super(vn, added);
        this.status = status;
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "VNlistItem{" +
                "status=" + status +
                ", notes='" + notes + '\'' +
                '}';
    }
}
