package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields extends VNDBCommand {
    private int vote;
    private int status;
    private String notes;
    private int priority;

    public static Fields create(int vote, int status, String notes, int priority) {
        Fields fields = new Fields();
        fields.vote = vote;
        fields.status = status;
        fields.notes = notes;
        fields.priority = priority;
        return fields;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
