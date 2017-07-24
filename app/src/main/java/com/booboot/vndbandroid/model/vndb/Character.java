package com.booboot.vndbandroid.model.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Character {
    private int id;
    private String name;
    private String original;
    private String gender;
    private String bloodt;
    private int[] birthday;
    private String aliases;
    private String description;
    private String image;
    private int bust;
    private int waist;
    private int hip;
    private int height;
    private int weight;
    private List<int[]> traits = new ArrayList<>();
    private List<Object[]> vns = new ArrayList<>();
    private List<CharacterVoiced> voiced = new ArrayList<>();

    public final static LinkedHashMap<String, String> ROLES = new LinkedHashMap<>();
    public static List<String> ROLES_KEYS;
    public final static int ROLE_INDEX = 3;

    static {
        /* Keep the order here : characters' roles are sorted in the same order as below */
        ROLES.put("main", "Protagonist");
        ROLES.put("primary", "Main character");
        ROLES.put("side", "Side character");
        ROLES.put("appears", "Makes an appearance");

        ROLES_KEYS = new ArrayList<>(ROLES.keySet());
    }

    public Character() {
    }

    public Character(int id) {
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

    public String getBloodt() {
        return bloodt;
    }

    public void setBloodt(String bloodt) {
        this.bloodt = bloodt == null ? bloodt : bloodt.toUpperCase();
    }

    public int[] getBirthday() {
        return birthday;
    }

    public void setBirthday(int[] birthday) {
        this.birthday = birthday;
    }

    public int getBust() {
        return bust;
    }

    public void setBust(int bust) {
        this.bust = bust;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public int getHip() {
        return hip;
    }

    public void setHip(int hip) {
        this.hip = hip;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<int[]> getTraits() {
        return traits;
    }

    public void setTraits(List<int[]> traits) {
        this.traits = traits;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Object[]> getVns() {
        return vns;
    }

    public void setVns(List<Object[]> vns) {
        if (vns == null) {
            this.vns = new ArrayList<>();
        } else {
            this.vns = vns;
        }
    }

    public List<CharacterVoiced> getVoiced() {
        return voiced;
    }

    public void setVoiced(List<CharacterVoiced> voiced) {
        this.voiced = voiced;
    }
}
