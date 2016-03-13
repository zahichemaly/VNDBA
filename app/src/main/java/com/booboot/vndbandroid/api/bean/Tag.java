package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag extends VNDBCommand {
    private int tag_id;
    private double score;
    private int spoiler_level;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getSpoiler_level() {
        return spoiler_level;
    }

    public void setSpoiler_level(int spoiler_level) {
        this.spoiler_level = spoiler_level;
    }
}
