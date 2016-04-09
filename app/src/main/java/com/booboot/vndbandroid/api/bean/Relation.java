package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relation extends VNDBCommand {
    public final static LinkedHashMap<String, String> TYPES = new LinkedHashMap<>();

    static {
        TYPES.put("seq", "Sequel");
        TYPES.put("preq", "Prequel");
        TYPES.put("ser", "Same series");
        TYPES.put("set", "Same setting");
        TYPES.put("side", "Side story");
        TYPES.put("par", "Parent story");
        TYPES.put("char", "Shares characters");
        TYPES.put("alt", "Alternative version");
        TYPES.put("fan", "Fandisc");
        TYPES.put("orig", "Original game");
    }

    private int id;
    private String relation;
    private String title;
    private String original;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
