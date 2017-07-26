package com.booboot.vndbandroid.model.vndb;

import android.content.Context;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.Utils;
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
public class Trait {
    private static Map<Integer, Trait> traits;

    private List<Integer> parents;
    private int id;
    private List<String> aliases;
    private int chars;
    private String name;
    private String description;
    private boolean meta;

    public static Map<Integer, Trait> getTraits(Context context) {
        if (traits != null) return traits;

        traits = new HashMap<>();
        InputStream ins = context.getResources().openRawResource(context.getResources().getIdentifier("traits", "raw", context.getPackageName()));
        try {
            List<Trait> traits_list = JSON.mapper.readValue(ins, new TypeReference<List<Trait>>() {
            });
            for (Trait trait : traits_list) {
                traits.put(trait.getId(), trait);
            }
        } catch (IOException e) {
            Utils.processException(e);
        }

        return traits;
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

    public int getChars() {
        return chars;
    }

    public void setChars(int chars) {
        this.chars = chars;
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

    @Override
    public String toString() {
        return name;
    }
}
