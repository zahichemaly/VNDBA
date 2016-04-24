package com.booboot.vndbandroid.factory;

import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.adapter.vndetails.VNDetailsElement;
import com.booboot.vndbandroid.api.bean.Anime;
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
import java.util.HashMap;
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
    public final static String TITLE_ANIME = "Anime";
    public final static String TITLE_PLATFORMS = "Platforms";
    public final static String TITLE_LANGUAGES = "Languages";

    public static LinkedHashMap<String, VNDetailsElement> getData(VNDetailsActivity activity) {
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = new LinkedHashMap<>();
        Item vn = activity.getVn();

        List<String> infoLeft = new ArrayList<>();
        List<String> infoRight = new ArrayList<>();
        List<Integer> infoRightImages = new ArrayList<>();
        infoLeft.add("Title");
        infoRight.add(vn.getTitle());
        infoRightImages.add(-1);
        if (vn.getOriginal() != null) {
            infoLeft.add("Original title");
            infoRight.add(vn.getOriginal());
            infoRightImages.add(-1);
        }

        infoLeft.add("Released date");
        if (vn.getReleased() == null) {
            infoRight.add("Unknown");
        } else {
            try {
                Date released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(vn.getReleased());
                infoRight.add(new SimpleDateFormat("d MMMM yyyy", Locale.US).format(released));
            } catch (ParseException e) {
                infoRight.add(vn.getReleased());
            }
        }
        infoRightImages.add(-1);

        if (vn.getAliases() != null) {
            infoLeft.add("Aliases");
            infoRight.add(vn.getAliases().replace("\n", "<br>"));
            infoRightImages.add(-1);
        }

        infoLeft.add("Length");
        infoRight.add(vn.getLengthString());
        infoRightImages.add(vn.getLengthImage());

        infoLeft.add("Links");
        Links links = vn.getLinks();
        String htmlLinks = "";
        if (links.getWikipedia() != null) htmlLinks += "<a href=\"" + Links.WIKIPEDIA + links.getWikipedia() + "\">Wikipedia</a>";
        if (links.getEncubed() != null) htmlLinks += "<br><a href=\"" + Links.ENCUBED + links.getEncubed() + "\">Encubed</a>";
        if (links.getRenai() != null) htmlLinks += "<br><a href=\"" + Links.RENAI + links.getRenai() + "\">Renai</a>";
        infoRight.add(htmlLinks);
        infoRightImages.add(-1);

        List<String> description = new ArrayList<>();
        description.add(vn.getDescription());

        List<String> genres = new ArrayList<>();
        for (List<Number> tag : vn.getTags()) {
            String tagName = Tag.getTags(activity).get(tag.get(0).intValue()).getName();
            if (Genre.contains(tagName)) {
                genres.add(tagName);
            }
        }

        if (activity.getCharacters() == null) {
            activity.setCharacterElement(new VNDetailsElement(null, new ArrayList<String>(), null, null, null, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            HashMap<String, List<String>> characters = getCharacters(activity);
            activity.setCharacterElement(new VNDetailsElement(null, characters.get("character_names"), characters.get("character_subnames"), null, characters.get("character_images"), VNDetailsElement.TYPE_SUBTITLE));
        }

        List<String> tags = new ArrayList<>();
        List<Integer> tags_images = new ArrayList<>();
        List<String> alreadyProcessedTags = new ArrayList<>();
        for (List<Number> cat : vn.getTags()) {
            String currentCategory = Tag.getTags(activity).get(cat.get(0).intValue()).getCat();
            if (!alreadyProcessedTags.contains(currentCategory)) {
                alreadyProcessedTags.add(currentCategory);
                tags.add("<b>" + Category.CATEGORIES.get(currentCategory) + " :</b>");
                tags_images.add(-1);
                for (List<Number> tag : vn.getTags()) {
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
        for (Relation relation : vn.getRelations()) {
            relation_titles.add(relation.getTitle());
            relation_types.add(Relation.TYPES.get(relation.getRelation()));
            relation_ids.add(relation.getId());
        }

        List<Integer> anime_ids = new ArrayList<>();
        List<String> anime_primary = new ArrayList<>();
        List<String> anime_secondary = new ArrayList<>();
        for (Anime anime : vn.getAnime()) {
            anime_ids.add(anime.getId());
            anime_primary.add(anime.getTitle_romaji());
            String title_kanji = anime.getTitle_kanji() != null ? anime.getTitle_kanji() + "\n" : "";
            String type = anime.getType() != null ? anime.getType() + " • " : "";
            String year = anime.getYear() > 0 ? anime.getYear() + "" : "";
            anime_secondary.add(title_kanji + type + year);
        }

        List<String> screenshots = new ArrayList<>();
        for (Screen screenshot : vn.getScreens()) {
            screenshots.add(screenshot.getImage());
        }

        List<String> statLeft = new ArrayList<>();
        List<String> statRight = new ArrayList<>();
        List<Integer> statRightImages = new ArrayList<>();
        statLeft.add("Popularity");
        statRight.add(vn.getPopularity() + "%");
        statRightImages.add(vn.getPopularityImage());
        statLeft.add("Rating");
        statRight.add(vn.getRating() + " (" + Vote.getName(vn.getRating()) + ")<br>" + vn.getVotecount() + " votes total");
        statRightImages.add(vn.getRatingImage());

        List<String> platforms = new ArrayList<>();
        List<Integer> platforms_images = new ArrayList<>();
        for (String platform : vn.getPlatforms()) {
            platforms.add(Platform.FULL_TEXT.get(platform));
            platforms_images.add(Platform.IMAGES.get(platform));
        }

        List<String> languages = new ArrayList<>();
        List<Integer> languages_flags = new ArrayList<>();
        for (String language : vn.getLanguages()) {
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
        expandableListDetail.put(TITLE_ANIME, new VNDetailsElement(anime_ids, anime_primary, anime_secondary, null, null, VNDetailsElement.TYPE_SUBTITLE));
        expandableListDetail.put(TITLE_PLATFORMS, new VNDetailsElement(platforms_images, platforms, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_LANGUAGES, new VNDetailsElement(languages_flags, languages, null, null, null, VNDetailsElement.TYPE_TEXT));

        return expandableListDetail;
    }

    public static HashMap<String, List<String>> getCharacters(VNDetailsActivity activity) {
        List<String> character_images = new ArrayList<>();
        List<String> character_names = new ArrayList<>();
        List<String> character_subnames = new ArrayList<>();
        for (Item character : activity.getCharacters()) {
            character_images.add(character.getImage());
            character_names.add(character.getName());
            character_subnames.add(Item.ROLES.get(character.getVns().get(0)[Item.ROLE_INDEX].toString()));
        }

        HashMap<String, List<String>> res = new HashMap<>();
        res.put("character_images", character_images);
        res.put("character_names", character_names);
        res.put("character_subnames", character_subnames);
        return res;
    }
}