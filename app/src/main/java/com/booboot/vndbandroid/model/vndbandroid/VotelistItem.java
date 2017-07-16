package com.booboot.vndbandroid.model.vndbandroid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VotelistItem extends CacheItem {
    protected int vote;

    public VotelistItem() {
    }

    public VotelistItem(int vn, int added, int vote) {
        super(vn, added);
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
