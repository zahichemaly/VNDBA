package com.booboot.vndbandroid.api.bean;

import android.content.Context;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.SettingsManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag extends VNDBCommand {
    private static Map<Integer, Tag> tags;

    private List<Integer> parents;
    private int id;
    private List<String> aliases;
    private int vns;
    private String cat;
    private String name;
    private String description;
    private boolean meta;

    public static Map<Integer, Tag> getTags(Context context) {
        if (tags != null) return tags;

        tags = new HashMap<>();
        InputStream ins = context.getResources().openRawResource(context.getResources().getIdentifier("tags", "raw", context.getPackageName()));
        try {
            List<Tag> tags_list = JSON.mapper.readValue(ins, new TypeReference<List<Tag>>() {
            });
            for (Tag tag : tags_list) {
                tags.put(tag.getId(), tag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tags;
    }

    public static boolean checkSpoilerLevel(VNDetailsActivity activity, int level) {
        int authorizedLevel = activity.spoilerLevel;
        if (authorizedLevel == 2) return true;
        return level < authorizedLevel + 1;
    }

    public List<Integer> getParents() {
        return parents;
    }

    public void setParents(List<Integer> parents) {
        this.parents = parents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public int getVns() {
        return vns;
    }

    public void setVns(int vns) {
        this.vns = vns;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMeta() {
        return meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    public static Integer getScoreImage(List<Number> tag) {
        float score = tag.get(1).floatValue();
        if (score >= 2) return R.drawable.score_green;
        if (score >= 1) return R.drawable.score_light_green;
        if (score >= 0) return R.drawable.score_yellow;
        return R.drawable.score_red;
    }
}
