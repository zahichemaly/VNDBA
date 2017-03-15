package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 06/04/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbStats extends VNDBCommand {
    private int users;
    private int threads;
    private int tags;
    private int releases;
    private int producers;
    private int chars;
    private int posts;
    private int vn;
    private int traits;

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getTags() {
        return tags;
    }

    public void setTags(int tags) {
        this.tags = tags;
    }

    public int getReleases() {
        return releases;
    }

    public void setReleases(int releases) {
        this.releases = releases;
    }

    public int getProducers() {
        return producers;
    }

    public void setProducers(int producers) {
        this.producers = producers;
    }

    public int getChars() {
        return chars;
    }

    public void setChars(int chars) {
        this.chars = chars;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getVn() {
        return vn;
    }

    public void setVn(int vn) {
        this.vn = vn;
    }

    public int getTraits() {
        return traits;
    }

    public void setTraits(int traits) {
        this.traits = traits;
    }
}
