package com.booboot.vndbandroid.api.bean;

import com.booboot.vndbandroid.R;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends VNDBCommand {
    private int id;
    private String title;
    private String original;
    private String released;
    private List<String> languages;
    private List<String> orig_lang;
    private List<String> platforms;
    private String aliases;
    private int length;
    private String description;
    private Links links;
    private String image;
    private boolean image_nsfw;
    private List<Anime> anime;
    private List<Relation> relations;
    private List<List<Number>> tags;
    private double popularity;
    private double rating;
    private int votecount;
    private int vn;
    private int status;
    private int added;
    private String notes;
    private int vote;
    private int priority;

    /* Character-specific properties */
    private String name;
    private String gender;
    private String bloodt;
    private int[] birthday;
    private int bust;
    private int waist;
    private int hip;
    private int height;
    private int weight;
    private List<int[]> traits;

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

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getVn() {
        return vn;
    }

    public void setVn(int vn) {
        this.vn = vn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getOrig_lang() {
        return orig_lang;
    }

    public void setOrig_lang(List<String> orig_lang) {
        this.orig_lang = orig_lang;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isImage_nsfw() {
        return image_nsfw;
    }

    public void setImage_nsfw(boolean image_nsfw) {
        this.image_nsfw = image_nsfw;
    }

    public List<Anime> getAnime() {
        return anime;
    }

    public void setAnime(List<Anime> anime) {
        this.anime = anime;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        Collections.sort(relations, new Comparator<Relation>() {
            @Override
            public int compare(Relation lhs, Relation rhs) {
                return Integer.valueOf(Relation.TYPES_KEY.indexOf(lhs.getRelation())).compareTo(Relation.TYPES_KEY.indexOf(rhs.getRelation()));
            }
        });
        this.relations = relations;
    }

    public List<List<Number>> getTags() {
        return tags;
    }

    public void setTags(List<List<Number>> tags) {
        Collections.sort(tags, new Comparator<List<Number>>() {
            @Override
            public int compare(List<Number> lhs, List<Number> rhs) {
                return Double.valueOf(rhs.get(1).doubleValue()).compareTo(lhs.get(1).doubleValue());
            }
        });
        this.tags = tags;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    private List<Screen> screens;

    public String getLengthString() {
        switch (length) {
            case 1:
                return "Very short (< 2 hours)";
            case 2:
                return "Short (2 - 10 hours)";
            case 3:
                return "Medium (10 - 30 hours)";
            case 4:
                return "Long (30 - 50 hours)";
            case 5:
                return "Very long (> 50 hours)";
            default:
                return "Unknown";
        }
    }

    public int getLengthImage() {
        switch (length) {
            case 1:
                return R.drawable.score_green;
            case 2:
                return R.drawable.score_light_green;
            case 3:
                return R.drawable.score_yellow;
            case 4:
                return R.drawable.score_orange;
            case 5:
                return R.drawable.score_red;
            default:
                return -1;
        }
    }

    public Integer getPopularityImage() {
        if (popularity >= 60) return R.drawable.score_green;
        if (popularity >= 40) return R.drawable.score_light_green;
        if (popularity >= 20) return R.drawable.score_yellow;
        if (popularity >= 10) return R.drawable.score_light_orange;
        if (popularity >= 1) return R.drawable.score_orange;
        else return R.drawable.score_red;
    }

    public Integer getRatingImage() {
        return Vote.getImage(rating);
    }
}
