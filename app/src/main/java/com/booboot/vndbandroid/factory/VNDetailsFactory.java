package com.booboot.vndbandroid.factory;

import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.adapter.vndetails.VNDetailsElement;
import com.booboot.vndbandroid.api.bean.Category;
import com.booboot.vndbandroid.api.bean.Genre;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Language;
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.api.bean.Platform;
import com.booboot.vndbandroid.api.bean.Relation;
import com.booboot.vndbandroid.api.bean.Screen;
import com.booboot.vndbandroid.api.bean.Tag;
import com.booboot.vndbandroid.api.bean.Vote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class VNDetailsFactory {
    public final static String TITLE_INFORMATION = "Information";
    public final static String TITLE_DESCRIPTION = "Description";
    public final static String TITLE_GENRES = "Genres";
    public final static String TITLE_CHARACTERS = "Characters";
    public final static String TITLE_SCREENSHOTS = "Screenshots";
    public final static String TITLE_STATS = "Stats";
    public final static String TITLE_TAGS = "Tags";
    public final static String TITLE_RELATIONS = "Relations";
    public final static String TITLE_PLATFORMS = "Platforms";
    public final static String TITLE_LANGUAGES = "Languages";

    public static LinkedHashMap<String, VNDetailsElement> getData(VNDetailsActivity activity) {
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = new LinkedHashMap<>();

        List<String> infoLeft = new ArrayList<>();
        List<String> infoRight = new ArrayList<>();
        List<Integer> infoRightImages = new ArrayList<>();
        infoLeft.add("Title");
        infoRight.add(activity.getVn().getTitle());
        infoRightImages.add(-1);
        if (activity.getVn().getOriginal() != null) {
            infoLeft.add("Original title");
            infoRight.add(activity.getVn().getOriginal());
            infoRightImages.add(-1);
        }

        try {
            infoLeft.add("Released date");
            Date released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(activity.getVn().getReleased());
            infoRight.add(new SimpleDateFormat("d MMMM yyyy", Locale.US).format(released));
            infoRightImages.add(-1);
        } catch (ParseException e) {
        }

        if (activity.getVn().getAliases() != null) {
            infoLeft.add("Aliases");
            infoRight.add(activity.getVn().getAliases().replace("\n", "<br>"));
            infoRightImages.add(-1);
        }

        infoLeft.add("Length");
        infoRight.add(activity.getVn().getLengthString());
        infoRightImages.add(activity.getVn().getLengthImage());

        infoLeft.add("Links");
        Links links = activity.getVn().getLinks();
        String htmlLinks = "";
        if (links.getWikipedia() != null) htmlLinks += "<a href=\"" + Links.WIKIPEDIA + links.getWikipedia() + "\">Wikipedia</a>";
        if (links.getEncubed() != null) htmlLinks += "<br><a href=\"" + Links.ENCUBED + links.getEncubed() + "\">Encubed</a>";
        if (links.getRenai() != null) htmlLinks += "<br><a href=\"" + Links.RENAI + links.getRenai() + "\">Renai</a>";
        infoRight.add(htmlLinks);
        infoRightImages.add(-1);

        List<String> description = new ArrayList<>();
        description.add(activity.getVn().getDescription());

        List<String> genres = new ArrayList<>();
        for (List<Number> tag : activity.getVn().getTags()) {
            String tagName = Tag.getTags(activity).get(tag.get(0).intValue()).getName();
            if (Genre.contains(tagName)) {
                genres.add(tagName);
            }
        }

        if (activity.getCharacters() == null) {
            activity.setCharacterElement(new VNDetailsElement(null, new ArrayList<String>(), null, null, null, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            List<String> character_images = new ArrayList<>();
            List<String> character_names = new ArrayList<>();
            List<String> character_subnames = new ArrayList<>();
            for (Item character : activity.getCharacters()) {
                character_images.add(character.getImage());
                character_names.add(character.getName());
                character_subnames.add(character.getOriginal());
            }
            activity.setCharacterElement(new VNDetailsElement(null, character_names, character_subnames, null, character_images, VNDetailsElement.TYPE_SUBTITLE));
        }

        List<String> tags = new ArrayList<>();
        List<Integer> tags_images = new ArrayList<>();
        List<String> alreadyProcessedTags = new ArrayList<>();
        for (List<Number> cat : activity.getVn().getTags()) {
            String currentCategory = Tag.getTags(activity).get(cat.get(0).intValue()).getCat();
            if (!alreadyProcessedTags.contains(currentCategory)) {
                alreadyProcessedTags.add(currentCategory);
                tags.add("<b>" + Category.CATEGORIES.get(currentCategory) + " :</b>");
                tags_images.add(-1);
                for (List<Number> tag : activity.getVn().getTags()) {
                    String tagCat = Tag.getTags(activity).get(tag.get(0).intValue()).getCat();
                    if (tagCat.equals(currentCategory)) {
                        String tagName = Tag.getTags(activity).get(tag.get(0).intValue()).getName();
                        tags.add(tagName);
                        tags_images.add(Tag.getScoreImage(tag));
                    }
                }
            }
        }

        List<Integer> relation_ids = new ArrayList<>();
        List<String> relation_titles = new ArrayList<>();
        List<String> relation_types = new ArrayList<>();
        for (Relation relation : activity.getVn().getRelations()) {
            relation_titles.add(relation.getTitle());
            relation_types.add(Relation.TYPES.get(relation.getRelation()));
            relation_ids.add(relation.getId());
        }

        List<String> screenshots = new ArrayList<>();
        for (Screen screenshot : activity.getVn().getScreens()) {
            screenshots.add(screenshot.getImage());
        }

        List<String> statLeft = new ArrayList<>();
        List<String> statRight = new ArrayList<>();
        List<Integer> statRightImages = new ArrayList<>();
        statLeft.add("Popularity");
        statRight.add(activity.getVn().getPopularity() + "%");
        statRightImages.add(activity.getVn().getPopularityImage());
        statLeft.add("Rating");
        statRight.add(activity.getVn().getRating() + " (" + Vote.getName(activity.getVn().getRating()) + ")<br>" + activity.getVn().getVotecount() + " votes total");
        statRightImages.add(activity.getVn().getRatingImage());

        List<String> platforms = new ArrayList<>();
        List<Integer> platforms_images = new ArrayList<>();
        for (String platform : activity.getVn().getPlatforms()) {
            platforms.add(Platform.FULL_TEXT.get(platform));
            platforms_images.add(Platform.IMAGES.get(platform));
        }

        List<String> languages = new ArrayList<>();
        List<Integer> languages_flags = new ArrayList<>();
        for (String language : activity.getVn().getLanguages()) {
            languages.add(Language.FULL_TEXT.get(language));
            languages_flags.add(Language.FLAGS.get(language));
        }

        expandableListDetail.put(TITLE_INFORMATION, new VNDetailsElement(null, infoLeft, infoRight, infoRightImages, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_DESCRIPTION, new VNDetailsElement(null, description, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_GENRES, new VNDetailsElement(null, genres, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_CHARACTERS, activity.getCharacterElement());
        expandableListDetail.put(TITLE_SCREENSHOTS, new VNDetailsElement(null, screenshots, null, null, null, VNDetailsElement.TYPE_IMAGES));
        expandableListDetail.put(TITLE_STATS, new VNDetailsElement(null, statLeft, statRight, statRightImages, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_TAGS, new VNDetailsElement(tags_images, tags, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_RELATIONS, new VNDetailsElement(relation_ids, relation_titles, relation_types, null, null, VNDetailsElement.TYPE_SUBTITLE));
        expandableListDetail.put(TITLE_PLATFORMS, new VNDetailsElement(platforms_images, platforms, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_LANGUAGES, new VNDetailsElement(languages_flags, languages, null, null, null, VNDetailsElement.TYPE_TEXT));

        return expandableListDetail;
    }
}