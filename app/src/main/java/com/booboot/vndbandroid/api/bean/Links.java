package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links extends VNDBCommand {
    public final static String WIKIPEDIA = "https://en.wikipedia.org/wiki/";
    public final static String ENCUBED = "http://novelnews.net/tag/";
    public final static String RENAI = "http://renai.us/game/";
    public final static String ANIDB = "https://anidb.net/perl-bin/animedb.pl?show=anime&aid=";
    public final static String VNDB_REGISTER = "https://vndb.org/u/register";
    public final static String VNDB = "https://vndb.org";
    public final static String VNDB_PAGE = "https://vndb.org/v";
    public final static String GITHUB = "https://github.com/herbeth1u/";
    public final static String MY_VNDB_PROFILE = "https://vndb.org/u104775";

    private String wikipedia;
    private String encubed;
    private String renai;

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public String getEncubed() {
        return encubed;
    }

    public void setEncubed(String encubed) {
        this.encubed = encubed;
    }

    public String getRenai() {
        return renai;
    }

    public void setRenai(String renai) {
        this.renai = renai;
    }
}
