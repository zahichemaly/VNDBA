package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

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
    private List<Object[]> aliases;
    private int main_alias;
    private List<StaffVns> vns = new ArrayList<>();
    private List<StaffVoiced> voiced = new ArrayList<>();
    private String note;

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

    public List<Object[]> getAliases() {
        return aliases;
    }

    public void setAliases(List<Object[]> aliases) {
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

    public int getMain_alias() {
        return main_alias;
    }

    public void setMain_alias(int main_alias) {
        this.main_alias = main_alias;
    }

    public List<StaffVns> getVns() {
        return vns;
    }

    public void setVns(List<StaffVns> vns) {
        this.vns = vns;
    }

    public List<StaffVoiced> getVoiced() {
        return voiced;
    }

    public void setVoiced(List<StaffVoiced> voiced) {
        this.voiced = voiced;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
