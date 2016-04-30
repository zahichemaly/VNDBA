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
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VNDetailsFactory {
    public final static String TITLE_INFORMATION = "Information";
    public final static String TITLE_DESCRIPTION = "Description";
    public final static String TITLE_GENRES = "Genres";
    public final static String TITLE_CHARACTERS = "Characters";
    public final static String TITLE_SCREENSHOTS = "Screenshots";
    public final static String TITLE_STATS = "Stats";
    public final static String TITLE_TAGS = "Tags";
    public final static String TITLE_RELEASES = "Releases";
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
        infoRight.add(Utils.getDate(vn.getReleased()));
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
        String descriptionWithoutSpoilers = vn.getDescription();
        if (!Tag.checkSpoilerLevel(2)) {
            descriptionWithoutSpoilers = descriptionWithoutSpoilers.replaceAll("\\[spoiler\\].*\\[/spoiler\\]", "");
        }
        description.add(descriptionWithoutSpoilers);

        List<String> genres = new ArrayList<>();
        for (List<Number> tagInfo : vn.getTags()) {
            if (!Tag.checkSpoilerLevel(tagInfo.get(2).intValue())) continue;
            Tag tag = Tag.getTags(activity).get(tagInfo.get(0).intValue());
            if (tag != null && Genre.contains(tag.getName())) {
                genres.add(tag.getName());
            }
        }

        if (activity.getCharacters() == null) {
            activity.setCharacterElement(new VNDetailsElement(null, new ArrayList<String>(), null, null, null, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            CharacterElementWrapper characters = getCharacters(activity);
            activity.setCharacterElement(new VNDetailsElement(null, characters.character_names, characters.character_subnames, null, characters.character_images, VNDetailsElement.TYPE_SUBTITLE));
        }

        if (activity.getReleases() == null) {
            activity.setReleaseElement(new VNDetailsElement(null, new ArrayList<String>(), null, null, null, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            ReleaseElementWrapper releases = getReleases(activity);
            activity.setReleaseElement(new VNDetailsElement(releases.release_images, releases.release_names, releases.release_subnames, releases.release_ids, null, VNDetailsElement.TYPE_SUBTITLE));
        }

        List<String> tags = new ArrayList<>();
        List<Integer> tags_images = new ArrayList<>();
        Map<String, Boolean> alreadyProcessedCategories = new HashMap<>();
        for (List<Number> catInfo : vn.getTags()) {
            if (!Tag.checkSpoilerLevel(catInfo.get(2).intValue())) continue;
            Tag cat = Tag.getTags(activity).get(catInfo.get(0).intValue());
            if (cat != null && alreadyProcessedCategories.get(cat.getCat()) == null) {
                alreadyProcessedCategories.put(cat.getCat(), true);
                tags.add("<b>" + Category.CATEGORIES.get(cat.getCat()) + " :</b>");
                tags_images.add(-1);
                for (List<Number> tagInfo : vn.getTags()) {
                    if (!Tag.checkSpoilerLevel(tagInfo.get(2).intValue())) continue;
                    Tag tag = Tag.getTags(activity).get(tagInfo.get(0).intValue());
                    if (tag != null && tag.getCat().equals(cat.getCat())) {
                        String tagName = Tag.getTags(activity).get(tagInfo.get(0).intValue()).getName();
                        tags.add(tagName);
                        tags_images.add(Tag.getScoreImage(tagInfo));
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
            if (screenshot.isNsfw() && !SettingsManager.getNSFW(activity)) continue;
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
        expandableListDetail.put(TITLE_RELEASES, activity.getReleaseElement());
        expandableListDetail.put(TITLE_RELATIONS, new VNDetailsElement(relation_ids, relation_titles, relation_types, null, null, VNDetailsElement.TYPE_SUBTITLE));
        expandableListDetail.put(TITLE_ANIME, new VNDetailsElement(anime_ids, anime_primary, anime_secondary, null, null, VNDetailsElement.TYPE_SUBTITLE));
        expandableListDetail.put(TITLE_PLATFORMS, new VNDetailsElement(platforms_images, platforms, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_LANGUAGES, new VNDetailsElement(languages_flags, languages, null, null, null, VNDetailsElement.TYPE_TEXT));

        return expandableListDetail;
    }

    public static CharacterElementWrapper getCharacters(VNDetailsActivity activity) {
        CharacterElementWrapper characterElementWrapper = new CharacterElementWrapper();
        for (Item character : activity.getCharacters()) {
            /* Checking the spoiler level of the whole character */
            boolean spoilerOk = true;
            /* Looping through the "vns" attribute because the spoiler lever is stored for each vn */
            for (Object[] spoilerInfo : character.getVns()) {
                /* Checking only the character for the current VN */
                if ((int) spoilerInfo[0] != activity.getVn().getId()) continue;
                /* If at least one release tags the character's spoiler level beyond our desired spoiler level, we totally hide it */
                if (!Tag.checkSpoilerLevel((int) spoilerInfo[2])) {
                    spoilerOk = false;
                    break;
                }
            }
            if (!spoilerOk) continue;

            characterElementWrapper.character_images.add(character.getImage());
            characterElementWrapper.character_names.add(character.getName());
            characterElementWrapper.character_subnames.add(Item.ROLES.get(character.getVns().get(0)[Item.ROLE_INDEX].toString()));
        }
        return characterElementWrapper;
    }

    public static ReleaseElementWrapper getReleases(VNDetailsActivity activity) {
        ReleaseElementWrapper releaseElementWrapper = new ReleaseElementWrapper();
        for (String language : activity.getReleases().keySet()) {
            releaseElementWrapper.release_images.add(Language.FLAGS.get(language));
            releaseElementWrapper.release_names.add("<b>" + Language.FULL_TEXT.get(language) + " :</b>");
            releaseElementWrapper.release_subnames.add(null);
            releaseElementWrapper.release_ids.add(null);
            for (Item release : activity.getReleases().get(language)) {
                releaseElementWrapper.release_images.add(null);
                releaseElementWrapper.release_names.add(release.getTitle());
                releaseElementWrapper.release_subnames.add(Utils.getDate(release.getReleased()) + " • " + Utils.capitalize(release.getType()));
                releaseElementWrapper.release_ids.add(release.getId());
            }
        }
        return releaseElementWrapper;
    }

    /**
     * CharacterElementWrapper and ReleaseElementWrapper are just wrapper classes for getCharacters() and getReleases(), because
     * Java is so stubborn that it's the nicer way to return all these elements from a method. We don't bother putting this in
     * a separate class because come on, there's already too many almost-empty beans in this project!
     */
    public static class CharacterElementWrapper {
        public List<String> character_images = new ArrayList<>();
        public List<String> character_names = new ArrayList<>();
        public List<String> character_subnames = new ArrayList<>();
    }

    public static class ReleaseElementWrapper {
        public List<Integer> release_images = new ArrayList<>();
        public List<String> release_names = new ArrayList<>();
        public List<String> release_subnames = new ArrayList<>();
        public List<Integer> release_ids = new ArrayList<>();
    }
}