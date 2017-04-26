package com.booboot.vndbandroid.model.vnstat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNStatItem {
    private List<SimilarNovel> similar;
    private List<SimilarNovel> recommendations;

    public List<SimilarNovel> getSimilar() {
        return similar;
    }

    public void setSimilar(List<SimilarNovel> similar) {
        this.similar = similar;
    }

    public List<SimilarNovel> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<SimilarNovel> recommendations) {
        this.recommendations = recommendations;
    }
}
