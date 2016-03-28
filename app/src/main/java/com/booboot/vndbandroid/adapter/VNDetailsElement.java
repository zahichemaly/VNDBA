package com.booboot.vndbandroid.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 26/03/2016.
 */
public class VNDetailsElement {
    public final static int TYPE_TEXT = 90;
    public final static int TYPE_IMAGES = 91;
    public final static int TYPE_TEXT_IMAGES = 92;

    private List<Integer> leftImages = new ArrayList<>();

    private List<String> data = new ArrayList<>();
    private int type;

    public VNDetailsElement(List<Integer> leftImages, List<String> data, int type) {
        this.leftImages = leftImages;
        this.data = data;
        this.type = type;
    }

    public List<String> getData() {

        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getLeftImages() {
        return leftImages;
    }

    public void setLeftImages(List<Integer> leftImages) {
        this.leftImages = leftImages;
    }

}
