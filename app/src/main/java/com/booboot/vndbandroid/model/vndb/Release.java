package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Release {
    private int id;
    private String title;
    private String original;
    private String released;
    private String type;
    private boolean patch;
    private boolean freeware;
    private boolean doujin;
    private List<String> languages;
    private String website;
    private String notes;
    private int minage;
    private String gtin;
    private String catalog;
    private List<String> platforms;
    private List<Media> media = new ArrayList<>();
    private String resolution;
    private int voiced;
    private int[] animation;
    private List<Producer> producers = new ArrayList<>();

    public Release() {
    }

    public Release(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        if (languages == null) {
            this.languages = new ArrayList<>();
        } else {
            this.languages = languages;
        }
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        if (platforms == null) {
            this.platforms = new ArrayList<>();
        } else {
            this.platforms = platforms;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPatch() {
        return patch;
    }

    public void setPatch(boolean patch) {
        this.patch = patch;
    }

    public boolean isFreeware() {
        return freeware;
    }

    public void setFreeware(boolean freeware) {
        this.freeware = freeware;
    }

    public boolean isDoujin() {
        return doujin;
    }

    public void setDoujin(boolean doujin) {
        this.doujin = doujin;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getMinage() {
        return minage;
    }

    public void setMinage(int minage) {
        this.minage = minage;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        if (media == null) {
            this.media = new ArrayList<>();
        } else {
            this.media = media;
        }
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public void setProducers(List<Producer> producers) {
        if (producers == null) {
            this.producers = new ArrayList<>();
        } else {
            this.producers = producers;
        }
    }

    public int getVoiced() {
        return voiced;
    }

    public void setVoiced(int voiced) {
        this.voiced = voiced;
    }
}
