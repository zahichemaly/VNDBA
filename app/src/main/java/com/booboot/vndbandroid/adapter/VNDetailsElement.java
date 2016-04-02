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
    private List<Integer> rightImages = new ArrayList<>();

    private List<String> leftData = new ArrayList<>();
    private List<String> rightData = new ArrayList<>();
    private int type;

    public VNDetailsElement(List<Integer> leftImages, List<String> leftData, List<String> rightData, List<Integer> rightImages, int type) {
        this.leftImages = leftImages;
        this.leftData = leftData;
        this.rightData = rightData;
        this.rightImages = rightImages;

        this.type = type;
    }

    public List<String> getLeftData() {
        return leftData;
    }

    public void setLeftData(List<String> leftData) {
        this.leftData = leftData;
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

    public List<String> getRightData() {
        return rightData;
    }

    public void setRightData(List<String> rightData) {
        this.rightData = rightData;
    }

    public List<Integer> getRightImages() {
        return rightImages;
    }

    public void setRightImages(List<Integer> rightImages) {
        this.rightImages = rightImages;
    }
}
