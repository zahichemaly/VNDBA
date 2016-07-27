package com.booboot.vndbandroid.bean.vnstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNStatItem {
    private List<SimilarNovel> similar;

    public List<SimilarNovel> getSimilar() {
        return similar;
    }

    public void setSimilar(List<SimilarNovel> similar) {
        this.similar = similar;
    }
}
