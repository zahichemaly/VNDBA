package com.booboot.vndbandroid.model.vndbandroid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CacheItem {
    protected int vn;
    protected int added;

    public CacheItem() {
    }

    public CacheItem(int vn, int added) {
        this.vn = vn;
        this.added = added;
    }

    public int getVn() {
        return vn;
    }

    public void setVn(int vn) {
        this.vn = vn;
    }

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheItem cacheItem = (CacheItem) o;
        return vn == cacheItem.vn;
    }

    @Override
    public int hashCode() {
        return vn;
    }
}
