package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links extends VNDBCommand {
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
