package com.booboot.vndbandroid.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Anime extends VNDBCommand {
    private int id;
    private int ann_id;
    private String nfo_id;
    private String title_romaji;
    private String title_kanji;
    private int year;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnn_id() {
        return ann_id;
    }

    public void setAnn_id(int ann_id) {
        this.ann_id = ann_id;
    }

    public String getNfo_id() {
        return nfo_id;
    }

    public void setNfo_id(String nfo_id) {
        this.nfo_id = nfo_id;
    }

    public String getTitle_romaji() {
        return title_romaji;
    }

    public void setTitle_romaji(String title_romaji) {
        this.title_romaji = title_romaji;
    }

    public String getTitle_kanji() {
        return title_kanji;
    }

    public void setTitle_kanji(String title_kanji) {
        this.title_kanji = title_kanji;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
