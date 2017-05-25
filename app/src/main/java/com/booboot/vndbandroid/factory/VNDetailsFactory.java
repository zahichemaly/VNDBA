package com.booboot.vndbandroid.factory;

import android.text.TextUtils;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.adapter.vndetails.VNDetailsElement;
import com.booboot.vndbandroid.model.vndb.Anime;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndb.Links;
import com.booboot.vndbandroid.model.vndb.Producer;
import com.booboot.vndbandroid.model.vndb.Relation;
import com.booboot.vndbandroid.model.vndb.Screen;
import com.booboot.vndbandroid.model.vndb.StaffSummary;
import com.booboot.vndbandroid.model.vndb.Tag;
import com.booboot.vndbandroid.model.vndbandroid.Category;
import com.booboot.vndbandroid.model.vndbandroid.Genre;
import com.booboot.vndbandroid.model.vndbandroid.Language;
import com.booboot.vndbandroid.model.vndbandroid.Platform;
import com.booboot.vndbandroid.model.vndbandroid.Vote;
import com.booboot.vndbandroid.model.vnstat.SimilarNovel;
import com.booboot.vndbandroid.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VNDetailsFactory {
    public final static String TITLE_INFORMATION = "Information";
    public final static String TITLE_DESCRIPTION = "Description";
    public final static String TITLE_GENRES = "Genres";
    public final static String TITLE_CHARACTERS = "Characters";
    public final static String TITLE_STAFF = "Staff";
    public final static String TITLE_SCREENSHOTS = "Screenshots";
    public final static String TITLE_STATS = "Stats";
    public final static String TITLE_TAGS = "Tags";
    public final static String TITLE_RELEASES = "Releases";
    public final static String TITLE_RELATIONS = "Relations";
    public final static String TITLE_SIMILAR_NOVELS = "Similar novels";
    public final static String TITLE_ANIME = "Anime";
    public final static String TITLE_PLATFORMS = "Platforms";
    public final static String TITLE_LANGUAGES = "Languages";

    public static LinkedHashMap<String, VNDetailsElement> getData(VNDetailsActivity activity) {
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = new LinkedHashMap<>();
        Item vn = activity.getVn();

        List<String> description = new ArrayList<>();
        if (vn.getDescription() != null) {
            String descriptionWithoutSpoilers = vn.getDescription();
            if (!Tag.checkSpoilerLevel(activity, 2)) {
                descriptionWithoutSpoilers = descriptionWithoutSpoilers.replaceAll("\\[spoiler\\][\\s\\S]*\\[/spoiler\\]", "");
            }
            description.add(descriptionWithoutSpoilers);
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

        setInformationSubmenu(activity);
        setGenresSubmenu(activity);
        setCharactersSubmenu(activity);
        setStaffSubmenu(activity);
        setReleasesSubmenu(activity);
        setTagsSubmenu(activity);
        setRelationsSubmenu(activity);
        setSimilarNovelsSubmenu(activity);
        setAnimesSubmenu(activity);
        setScreensSubmenu(activity);
        setPlatformsSubmenu(activity);
        setLanguagesSubmenu(activity);

        expandableListDetail.put(TITLE_INFORMATION, activity.getInformationSubmenu());
        expandableListDetail.put(TITLE_DESCRIPTION, new VNDetailsElement(null, description, null, null, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_GENRES, activity.getGenresSubmenu());
        expandableListDetail.put(TITLE_CHARACTERS, activity.getCharactersSubmenu());
        expandableListDetail.put(TITLE_STAFF, activity.getStaffSubmenu());
        expandableListDetail.put(TITLE_SCREENSHOTS, activity.getScreensSubmenu());
        expandableListDetail.put(TITLE_STATS, new VNDetailsElement(null, statLeft, statRight, statRightImages, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_TAGS, activity.getTagsSubmenu());
        expandableListDetail.put(TITLE_RELEASES, activity.getReleasesSubmenu());
        expandableListDetail.put(TITLE_RELATIONS, activity.getRelationsSubmenu());
        expandableListDetail.put(TITLE_SIMILAR_NOVELS, activity.getSimilarNovelsSubmenu());
        expandableListDetail.put(TITLE_ANIME, activity.getAnimesSubmenu());
        expandableListDetail.put(TITLE_PLATFORMS, activity.getPlatformsSubmenu());
        expandableListDetail.put(TITLE_LANGUAGES, activity.getLanguagesSubmenu());

        return expandableListDetail;
    }

    public static void setInformationSubmenu(VNDetailsActivity activity) {
        List<String> infoLeft = new ArrayList<>();
        List<String> infoRight = new ArrayList<>();
        List<Integer> infoRightImages = new ArrayList<>();

        if (activity.getReleases() == null) {
            activity.setInformationSubmenu(new VNDetailsElement(null, infoLeft, infoRight, infoRightImages, null, null, VNDetailsElement.TYPE_TEXT));
        } else {
            infoLeft.add("Title");
            infoRight.add(activity.getVn().getTitle());
            infoRightImages.add(-1);
            if (activity.getVn().getOriginal() != null) {
                infoLeft.add("Original title");
                infoRight.add(activity.getVn().getOriginal());
                infoRightImages.add(-1);
            }

            infoLeft.add("Released date");
            infoRight.add(Utils.getDate(activity.getVn().getReleased(), true));
            infoRightImages.add(-1);

            if (activity.getVn().getAliases() != null) {
                infoLeft.add("Aliases");
                infoRight.add(activity.getVn().getAliases().replace("\n", "<br>"));
                infoRightImages.add(-1);
            }

            infoLeft.add("Developers");
            Set<String> developers = new HashSet<>();
            for (List<Item> releases : activity.getReleases().values()) {
                for (Item release : releases) {
                    for (Producer producer : release.getProducers()) {
                        if (producer.isDeveloper()) {
                            developers.add(producer.getName());
                        }
                    }
                }
            }
            infoRight.add(developers.isEmpty() ? "-" : TextUtils.join(", ", developers));
            infoRightImages.add(-1);

            infoLeft.add("Length");
            infoRight.add(activity.getVn().getLengthString());
            infoRightImages.add(activity.getVn().getLengthImage());

            infoLeft.add("Links");
            Links links = activity.getVn().getLinks();
            String htmlLinks = "";
            if (links.getWikipedia() != null) htmlLinks += "[url=" + Links.WIKIPEDIA + links.getWikipedia() + "]Wikipedia[/url]";
            if (links.getEncubed() != null) htmlLinks += "<br>[url=" + Links.ENCUBED + links.getEncubed() + "]Encubed[/url]";
            if (links.getRenai() != null) htmlLinks += "<br>[url=" + Links.RENAI + links.getRenai() + "]Renai[/url]";
            infoRight.add(htmlLinks);
            infoRightImages.add(-1);

            if (activity.getInformationSubmenu() == null) {
                activity.setInformationSubmenu(new VNDetailsElement(null, infoLeft, infoRight, infoRightImages, null, null, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getInformationSubmenu().setPrimaryData(infoLeft);
                activity.getInformationSubmenu().setSecondaryData(infoRight);
                activity.getInformationSubmenu().setSecondaryImages(infoRightImages);
            }
        }
    }

    public static void setLanguagesSubmenu(VNDetailsActivity activity) {
        List<String> languages = new ArrayList<>();
        List<Integer> languages_flags = new ArrayList<>();

        if (activity.getVn().getLanguages() == null) {
            activity.setLanguagesSubmenu(new VNDetailsElement(languages_flags, languages, null, null, null, null, VNDetailsElement.TYPE_TEXT));
        } else {
            for (String language : activity.getVn().getLanguages()) {
                languages.add(Language.FULL_TEXT.get(language));
                languages_flags.add(Language.FLAGS.get(language));
            }

            if (activity.getLanguagesSubmenu() == null) {
                activity.setLanguagesSubmenu(new VNDetailsElement(languages_flags, languages, null, null, null, null, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getLanguagesSubmenu().setPrimaryImages(languages_flags);
                activity.getLanguagesSubmenu().setPrimaryData(languages);
            }
        }
    }

    public static void setPlatformsSubmenu(VNDetailsActivity activity) {
        List<String> platforms = new ArrayList<>();
        List<Integer> platforms_images = new ArrayList<>();

        if (activity.getVn().getPlatforms() == null) {
            activity.setPlatformsSubmenu(new VNDetailsElement(platforms_images, platforms, null, null, null, null, VNDetailsElement.TYPE_TEXT));
        } else {
            for (String platform : activity.getVn().getPlatforms()) {
                platforms.add(Platform.FULL_TEXT.get(platform));
                platforms_images.add(Platform.IMAGES.get(platform));
            }

            if (activity.getPlatformsSubmenu() == null) {
                activity.setPlatformsSubmenu(new VNDetailsElement(platforms_images, platforms, null, null, null, null, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getPlatformsSubmenu().setPrimaryImages(platforms_images);
                activity.getPlatformsSubmenu().setPrimaryData(platforms);
            }
        }
    }

    public static void setAnimesSubmenu(VNDetailsActivity activity) {
        List<Integer> anime_ids = new ArrayList<>();
        List<String> anime_primary = new ArrayList<>();
        List<String> anime_secondary = new ArrayList<>();

        if (activity.getVn().getAnime() == null) {
            activity.setAnimesSubmenu(new VNDetailsElement(null, anime_primary, anime_secondary, null, null, anime_ids, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (Anime anime : activity.getVn().getAnime()) {
                anime_ids.add(anime.getId());
                anime_primary.add(anime.getTitle_romaji());
                String title_kanji = anime.getTitle_kanji() != null ? anime.getTitle_kanji() + "\n" : "";
                String type = anime.getType() != null ? anime.getType() + activity.getString(R.string.bullet) : "";
                String year = anime.getYear() > 0 ? anime.getYear() + "" : "";
                anime_secondary.add(title_kanji + type + year);
            }

            if (activity.getAnimesSubmenu() == null) {
                activity.setAnimesSubmenu(new VNDetailsElement(null, anime_primary, anime_secondary, null, null, anime_ids, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getAnimesSubmenu().setIds(anime_ids);
                activity.getAnimesSubmenu().setPrimaryData(anime_primary);
                activity.getAnimesSubmenu().setSecondaryData(anime_secondary);
            }
        }
    }

    public static void setRelationsSubmenu(VNDetailsActivity activity) {
        List<Integer> relation_ids = new ArrayList<>();
        List<String> relation_titles = new ArrayList<>();
        List<String> relation_types = new ArrayList<>();

        if (activity.getVn().getRelations() == null) {
            activity.setRelationsSubmenu(new VNDetailsElement(null, relation_titles, relation_types, null, null, relation_ids, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (Relation relation : activity.getVn().getRelations()) {
                relation_titles.add(relation.getTitle());
                relation_types.add(Relation.TYPES.get(relation.getRelation()));
                relation_ids.add(relation.getId());
            }

            if (activity.getRelationsSubmenu() == null) {
                activity.setRelationsSubmenu(new VNDetailsElement(null, relation_titles, relation_types, null, null, relation_ids, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getRelationsSubmenu().setIds(relation_ids);
                activity.getRelationsSubmenu().setPrimaryData(relation_titles);
                activity.getRelationsSubmenu().setSecondaryData(relation_types);
            }
        }
    }

    public static void setSimilarNovelsSubmenu(VNDetailsActivity activity) {
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> subnames = new ArrayList<>();
        List<String> images = new ArrayList<>();
        List<Integer> rightImages = new ArrayList<>();

        if (activity.getSimilarNovels() == null) {
            activity.setSimilarNovelsSubmenu(new VNDetailsElement(null, names, subnames, rightImages, images, ids, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (SimilarNovel similarNovel : activity.getSimilarNovels()) {
                ids.add(similarNovel.getNovelId());
                names.add(similarNovel.getTitle());
                subnames.add("Similarity : " + similarNovel.getSimilarityPercentage() + "%");
                images.add(similarNovel.getImageLink());
                rightImages.add(similarNovel.getSimilarityImage());
            }

            if (activity.getSimilarNovelsSubmenu() == null) {
                activity.setSimilarNovelsSubmenu(new VNDetailsElement(null, names, subnames, rightImages, images, ids, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getSimilarNovelsSubmenu().setIds(ids);
                activity.getSimilarNovelsSubmenu().setPrimaryData(names);
                activity.getSimilarNovelsSubmenu().setSecondaryData(subnames);
                activity.getSimilarNovelsSubmenu().setUrlImages(images);
                activity.getSimilarNovelsSubmenu().setSecondaryImages(rightImages);
            }
        }
    }

    public static void setTagsSubmenu(VNDetailsActivity activity) {
        List<String> tags = new ArrayList<>();
        List<Integer> tags_images = new ArrayList<>();
        List<Integer> tags_ids = new ArrayList<>();

        if (activity.getVn().getTags() == null) {
            activity.setTagsSubmenu(new VNDetailsElement(tags_images, tags, null, null, null, tags_ids, VNDetailsElement.TYPE_TEXT));
        } else {
            Map<String, Boolean> alreadyProcessedCategories = new HashMap<>();
            for (List<Number> catInfo : activity.getVn().getTags()) {
                if (!Tag.checkSpoilerLevel(activity, catInfo.get(2).intValue())) continue;
                Tag cat = Tag.getTags(activity).get(catInfo.get(0).intValue());
                if (cat != null && alreadyProcessedCategories.get(cat.getCat()) == null) {
                    alreadyProcessedCategories.put(cat.getCat(), true);
                    tags.add("<b>" + Category.CATEGORIES.get(cat.getCat()) + " :</b>");
                    tags_images.add(-1);
                    tags_ids.add(-1);
                    for (List<Number> tagInfo : activity.getVn().getTags()) {
                        if (!Tag.checkSpoilerLevel(activity, tagInfo.get(2).intValue())) continue;
                        Tag tag = Tag.getTags(activity).get(tagInfo.get(0).intValue());
                        if (tag != null && tag.getCat().equals(cat.getCat())) {
                            tags.add(tag.getName());
                            tags_images.add(Tag.getScoreImage(tagInfo));
                            tags_ids.add(tag.getId());
                        }
                    }
                }
            }

            if (activity.getTagsSubmenu() == null) {
                activity.setTagsSubmenu(new VNDetailsElement(tags_images, tags, null, null, null, tags_ids, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getTagsSubmenu().setPrimaryImages(tags_images);
                activity.getTagsSubmenu().setPrimaryData(tags);
                activity.getTagsSubmenu().setIds(tags_ids);
            }
        }
    }

    public static void setGenresSubmenu(VNDetailsActivity activity) {
        List<String> genres = new ArrayList<>();

        if (activity.getVn().getTags() == null) {
            activity.setGenresSubmenu(new VNDetailsElement(null, genres, null, null, null, null, VNDetailsElement.TYPE_TEXT));
        } else {
            for (List<Number> tagInfo : activity.getVn().getTags()) {
                if (!Tag.checkSpoilerLevel(activity, tagInfo.get(2).intValue())) continue;
                Tag tag = Tag.getTags(activity).get(tagInfo.get(0).intValue());
                if (tag != null && Genre.contains(tag.getName())) {
                    genres.add(tag.getName());
                }
            }

            if (activity.getGenresSubmenu() == null) {
                activity.setGenresSubmenu(new VNDetailsElement(null, genres, null, null, null, null, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getGenresSubmenu().setPrimaryData(genres);
            }
        }
    }

    public static void setScreensSubmenu(VNDetailsActivity activity) {
        List<String> screenshots = new ArrayList<>();

        if (activity.getVn().getScreens() == null) {
            activity.setScreensSubmenu(new VNDetailsElement(null, screenshots, null, null, null, null, VNDetailsElement.TYPE_IMAGES));
        } else {
            for (Screen screenshot : activity.getVn().getScreens()) {
                if (screenshot.isNsfw() && activity.nsfwLevel <= 0) continue;
                screenshots.add(screenshot.getImage());
            }

            if (activity.getScreensSubmenu() == null) {
                activity.setScreensSubmenu(new VNDetailsElement(null, screenshots, null, null, null, null, VNDetailsElement.TYPE_IMAGES));
            } else {
                activity.getScreensSubmenu().setPrimaryData(screenshots);
            }
        }
    }

    public static void setCharactersSubmenu(VNDetailsActivity activity) {
        List<String> images = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> subnames = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        boolean canBuildMenu = activity.getCharacters() != null;

        /*
        boolean canBuildMenu = activity.getCharacters() != null && activity.getCharacters().getVoiced() != null;

        if (canBuildMenu) {
            for (StaffSummary staff : activity.getCharacters().getVoiced()) {
                if (Cache.staff.get(staff.getId()) == null) {
                    canBuildMenu = false;
                    break;
                }
            }
        }
*/

        if (!canBuildMenu) {
            activity.setCharactersSubmenu(new VNDetailsElement(null, names, subnames, null, images, ids, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (Item character : activity.getCharacters()) {
                /* Checking the spoiler level of the whole character */
                boolean spoilerOk = true;
                /* Looping through the "vns" attribute because the spoiler lever is stored for each vn */
                for (Object[] spoilerInfo : character.getVns()) {
                    /* Checking only the character for the current VN */
                    if ((int) spoilerInfo[0] != activity.getVn().getId()) continue;
                    /* If at least one release tags the character's spoiler level beyond our desired spoiler level, we totally hide it */
                    if (!Tag.checkSpoilerLevel(activity, (int) spoilerInfo[2])) {
                        spoilerOk = false;
                        break;
                    }
                }
                if (!spoilerOk) continue;

                images.add(character.getImage());
                names.add(character.getName());
                subnames.add(Item.ROLES.get(character.getVns().get(0)[Item.ROLE_INDEX].toString()));
                ids.add(character.getId());
            }

            if (activity.getCharactersSubmenu() == null) {
                activity.setCharactersSubmenu(new VNDetailsElement(null, names, subnames, null, images, ids, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getCharactersSubmenu().setIds(ids);
                activity.getCharactersSubmenu().setPrimaryData(names);
                activity.getCharactersSubmenu().setSecondaryData(subnames);
                activity.getCharactersSubmenu().setUrlImages(images);
            }
        }
    }

    public static void setStaffSubmenu(VNDetailsActivity activity) {
        List<String> staffNames = new ArrayList<>();
        List<String> staffNotes = new ArrayList<>();
        List<Integer> staffIds = new ArrayList<>();
        List<Integer> staffIcons = new ArrayList<>();

        if (activity.getVn().getStaff() == null) {
            activity.setStaffSubmenu(new VNDetailsElement(staffIcons, staffNames, staffNotes, null, null, staffIds, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            Map<String, Boolean> alreadyProcessedRoles = new HashMap<>();
            for (StaffSummary staff : activity.getVn().getStaff()) {
                String role = staff.getRole();
                if (role != null && alreadyProcessedRoles.get(role) == null) {
                    alreadyProcessedRoles.put(role, true);
                    staffNames.add("<b>" + Category.CATEGORIES.get(role) + " :</b>");
                    staffNotes.add(null);
                    staffIds.add(-1);
                    staffIcons.add(-1);
                    for (StaffSummary staffInfo : activity.getVn().getStaff()) {
                        if (staffInfo != null && staffInfo.getRole().equals(role)) {
                            staffNames.add(staffInfo.getName());
                            staffNotes.add(staffInfo.getNote());
                            staffIds.add(staffInfo.getId());
                            staffIcons.add(staffInfo.getIcon());
                        }
                    }
                }
            }

            if (activity.getStaffSubmenu() == null) {
                activity.setStaffSubmenu(new VNDetailsElement(staffIcons, staffNames, staffNotes, null, null, staffIds, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getStaffSubmenu().setPrimaryImages(staffIcons);
                activity.getStaffSubmenu().setPrimaryData(staffNames);
                activity.getStaffSubmenu().setSecondaryData(staffNotes);
                activity.getStaffSubmenu().setIds(staffIds);
            }
        }
    }

    public static void setReleasesSubmenu(VNDetailsActivity activity) {
        List<Integer> images = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> subnames = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        if (activity.getReleases() == null) {
            activity.setReleasesSubmenu(new VNDetailsElement(images, names, subnames, null, null, ids, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (String language : activity.getReleases().keySet()) {
                images.add(Language.FLAGS.get(language));
                names.add("<b>" + Language.FULL_TEXT.get(language) + " :</b>");
                subnames.add(null);
                ids.add(-1);
                for (Item release : activity.getReleases().get(language)) {
                    images.add(-1);
                    names.add(release.getTitle());
                    subnames.add(Utils.getDate(release.getReleased(), true) + activity.getString(R.string.bullet) + Utils.capitalize(release.getType()));
                    ids.add(release.getId());
                }

                if (activity.getReleasesSubmenu() == null) {
                    activity.setReleasesSubmenu(new VNDetailsElement(images, names, subnames, null, null, ids, VNDetailsElement.TYPE_SUBTITLE));
                } else {
                    activity.getReleasesSubmenu().setPrimaryImages(images);
                    activity.getReleasesSubmenu().setPrimaryData(names);
                    activity.getReleasesSubmenu().setSecondaryData(subnames);
                    activity.getReleasesSubmenu().setIds(ids);
                }
            }
        }
    }
}