package com.booboot.vndbandroid.adapter.vndetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 26/03/2016.
 */
public class VNDetailsElement {
    public final static int TYPE_TEXT = 90;
    public final static int TYPE_IMAGES = 91;
    public final static int TYPE_CUSTOM = 92;
    public final static int TYPE_SUBTITLE = 93;

    private List<Integer> primaryImages = new ArrayList<>();
    private List<Integer> secondaryImages = new ArrayList<>();
    private List<String> urlImages = new ArrayList<>();

    public List<String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }

    private List<String> primaryData = new ArrayList<>();
    private List<String> secondaryData = new ArrayList<>();
    private int type;

    public VNDetailsElement(List<Integer> primaryImages, List<String> primaryData, List<String> secondaryData, List<Integer> secondaryImages, List<String> urlImages, int type) {
        this.primaryImages = primaryImages;
        this.primaryData = primaryData;
        this.secondaryData = secondaryData;
        this.secondaryImages = secondaryImages;
        this.urlImages = urlImages;

        this.type = type;
    }

    public List<String> getPrimaryData() {
        return primaryData;
    }

    public void setPrimaryData(List<String> primaryData) {
        this.primaryData = primaryData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getPrimaryImages() {
        return primaryImages;
    }

    public void setPrimaryImages(List<Integer> primaryImages) {
        this.primaryImages = primaryImages;
    }

    public List<String> getSecondaryData() {
        return secondaryData;
    }

    public void setSecondaryData(List<String> secondaryData) {
        this.secondaryData = secondaryData;
    }

    public List<Integer> getSecondaryImages() {
        return secondaryImages;
    }

    public void setSecondaryImages(List<Integer> secondaryImages) {
        this.secondaryImages = secondaryImages;
    }
}
