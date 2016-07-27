package com.booboot.vndbandroid.bean.vnstat;

import com.booboot.vndbandroid.R;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimilarNovel {
    public final static String IMAGE_LINK = "http://i.vnstat.net/";
    private int novelId;
    private double similarity;
    private String title;
    private String votecount;
    private String popularity;
    private String bayesianRating;
    private String meanRating;
    private String released;
    private String image;

    public String getImageLink() {
        return IMAGE_LINK + image;
    }

    public double getSimilarityPercentage() {
        return Math.floor(similarity * 1000 + 0.5) / 10;
    }


    public Integer getSimilarityImage() {
        if (similarity >= 0.6) return R.drawable.score_green;
        if (similarity >= 0.4) return R.drawable.score_light_green;
        if (similarity >= 0.2) return R.drawable.score_yellow;
        if (similarity >= 0.1) return R.drawable.score_light_orange;
        if (similarity >= 0.01) return R.drawable.score_orange;
        else return R.drawable.score_red;
    }

    public int getNovelId() {
        return novelId;
    }

    public void setNovelId(String novelId) {
        this.novelId = Integer.parseInt(novelId);
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = Double.parseDouble(similarity);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVotecount() {
        return votecount;
    }

    public void setVotecount(String votecount) {
        this.votecount = votecount;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getBayesianRating() {
        return bayesianRating;
    }

    public void setBayesianRating(String bayesianRating) {
        this.bayesianRating = bayesianRating;
    }

    public String getMeanRating() {
        return meanRating;
    }

    public void setMeanRating(String meanRating) {
        this.meanRating = meanRating;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
