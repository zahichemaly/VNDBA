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

    private int type;
    private List<Data> data = new ArrayList<>();

    public static class Data {
        int id = -1, image1 = -1, image2 = -1, button = -1;
        String urlImage, text1, text2;

        public Data setId(int id) {
            this.id = id;
            return this;
        }

        public Data setImage1(int image1) {
            this.image1 = image1;
            return this;
        }

        public Data setImage2(int image2) {
            this.image2 = image2;
            return this;
        }

        public Data setUrlImage(String urlImage) {
            this.urlImage = urlImage;
            return this;
        }

        public Data setText1(String text1) {
            this.text1 = text1;
            return this;
        }

        public Data setText2(String text2) {
            this.text2 = text2;
            return this;
        }

        public Data setButton(int button) {
            this.button = button;
            return this;
        }
    }

    public VNDetailsElement(List<Data> data, int type) {
        this.data = data;
        this.type = type;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public VNDetailsElement setType(int type) {
        this.type = type;
        return this;
    }
}
