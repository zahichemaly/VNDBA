package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Staff {
    private int id;
    private String name;
    private String original;
    private String gender;
    private String language;
    private Links links;
    private String description;
    private String aliases;
    private String main_alias;
    private StaffVns vns;
    private StaffVoiced voiced;

    public Staff() {
    }

    public Staff(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? gender : gender.toUpperCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMain_alias() {
        return main_alias;
    }

    public void setMain_alias(String main_alias) {
        this.main_alias = main_alias;
    }

    public StaffVns getVns() {
        return vns;
    }

    public void setVns(StaffVns vns) {
        this.vns = vns;
    }

    public StaffVoiced getVoiced() {
        return voiced;
    }

    public void setVoiced(StaffVoiced voiced) {
        this.voiced = voiced;
    }
}
