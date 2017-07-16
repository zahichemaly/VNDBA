package com.booboot.vndbandroid.model.vndbandroid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WishlistItem extends CacheItem {
    protected int priority;

    public WishlistItem() {
    }

    public WishlistItem(int vn, int added, int priority) {
        super(vn, added);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}