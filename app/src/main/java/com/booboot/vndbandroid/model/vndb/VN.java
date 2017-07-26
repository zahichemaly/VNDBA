package com.booboot.vndbandroid.model.vndb;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.model.vndbandroid.Vote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VN {
    private int id;
    private String title;
    private String original;
    private String released;
    /* WARNING: don't initialize these lists because there are important null checks on them (checking whether they're already init in VNDetailsActivity.initSubmenu() */
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
    private List<Screen> screens;
    private List<VnStaff> staff;

    public VN() {
    }

    public VN(int id) {
        this.id = id;
    }

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
        if (languages == null) {
            this.languages = new ArrayList<>();
        } else {
            this.languages = languages;
        }
    }

    public List<String> getOrig_lang() {
        return orig_lang;
    }

    public void setOrig_lang(List<String> orig_lang) {
        if (orig_lang == null) {
            this.orig_lang = new ArrayList<>();
        } else {
            this.orig_lang = orig_lang;
        }
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        if (platforms == null) {
            this.platforms = new ArrayList<>();
        } else {
            this.platforms = platforms;
        }
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
        if (anime == null) {
            this.anime = new ArrayList<>();
        } else {
            this.anime = anime;
        }
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        if (relations == null) {
            this.relations = new ArrayList<>();
        } else {
            Collections.sort(relations, new Comparator<Relation>() {
                @Override
                public int compare(Relation lhs, Relation rhs) {
                    return Integer.valueOf(Relation.TYPES_KEY.indexOf(lhs.getRelation())).compareTo(Relation.TYPES_KEY.indexOf(rhs.getRelation()));
                }
            });
            this.relations = relations;
        }
    }

    public List<List<Number>> getTags() {
        return tags;
    }

    public void setTags(List<List<Number>> tags) {
        if (tags == null) {
            this.tags = new ArrayList<>();
        } else {
            Collections.sort(tags, new Comparator<List<Number>>() {
                @Override
                public int compare(List<Number> lhs, List<Number> rhs) {
                    return Double.valueOf(rhs.get(1).doubleValue()).compareTo(lhs.get(1).doubleValue());
                }
            });
            this.tags = tags;
        }
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
        if (screens == null) {
            this.screens = new ArrayList<>();
        } else {
            this.screens = screens;
        }
    }

    public List<VnStaff> getStaff() {
        return staff;
    }

    public void setStaff(List<VnStaff> staff) {
        this.staff = staff;
    }
}
