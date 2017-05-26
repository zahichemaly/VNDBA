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

        List<VNDetailsElement.Data> descriptionData = new ArrayList<>();
        if (vn.getDescription() != null) {
            String descriptionWithoutSpoilers = vn.getDescription();
            if (!Tag.checkSpoilerLevel(activity, 2)) {
                descriptionWithoutSpoilers = descriptionWithoutSpoilers.replaceAll("\\[spoiler\\][\\s\\S]*\\[/spoiler\\]", "");
            }
            descriptionData.add(new VNDetailsElement.Data().setText1(descriptionWithoutSpoilers));
        }

        List<VNDetailsElement.Data> statsData = new ArrayList<>();
        statsData.add(new VNDetailsElement.Data()
                .setText1("Popularity")
                .setText2(vn.getPopularity() + "%")
                .setImage2(vn.getPopularityImage()));
        statsData.add(new VNDetailsElement.Data()
                .setText1("Rating")
                .setText2(vn.getRating() + " (" + Vote.getName(vn.getRating()) + ")<br>" + vn.getVotecount() + " votes total")
                .setImage2(vn.getRatingImage()));

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
        expandableListDetail.put(TITLE_DESCRIPTION, new VNDetailsElement(descriptionData, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_GENRES, activity.getGenresSubmenu());
        expandableListDetail.put(TITLE_CHARACTERS, activity.getCharactersSubmenu());
        expandableListDetail.put(TITLE_STAFF, activity.getStaffSubmenu());
        expandableListDetail.put(TITLE_SCREENSHOTS, activity.getScreensSubmenu());
        expandableListDetail.put(TITLE_STATS, new VNDetailsElement(statsData, VNDetailsElement.TYPE_TEXT));
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
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getReleases() == null) {
            activity.setInformationSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
        } else {
            data.add(new VNDetailsElement.Data().setText1("Title").setText2(activity.getVn().getTitle()));
            if (activity.getVn().getOriginal() != null) {
                data.add(new VNDetailsElement.Data().setText1("Original title").setText2(activity.getVn().getOriginal()));
            }
            data.add(new VNDetailsElement.Data().setText1("Released date").setText2(Utils.getDate(activity.getVn().getReleased(), true)));

            if (activity.getVn().getAliases() != null) {
                data.add(new VNDetailsElement.Data().setText1("Aliases").setText2(activity.getVn().getAliases().replace("\n", "<br>")));
            }

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

            data.add(new VNDetailsElement.Data().setText1("Developers").setText2(developers.isEmpty() ? "-" : TextUtils.join(", ", developers)));
            data.add(new VNDetailsElement.Data().setText1("Length").setText2(activity.getVn().getLengthString()).setImage2(activity.getVn().getLengthImage()));

            Links links = activity.getVn().getLinks();
            String htmlLinks = "";
            if (links.getWikipedia() != null) htmlLinks += "[url=" + Links.WIKIPEDIA + links.getWikipedia() + "]Wikipedia[/url]";
            if (links.getEncubed() != null) htmlLinks += "<br>[url=" + Links.ENCUBED + links.getEncubed() + "]Encubed[/url]";
            if (links.getRenai() != null) htmlLinks += "<br>[url=" + Links.RENAI + links.getRenai() + "]Renai[/url]";
            data.add(new VNDetailsElement.Data().setText1("Links").setText2(htmlLinks));

            if (activity.getInformationSubmenu() == null) {
                activity.setInformationSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getInformationSubmenu().setData(data);
            }
        }
    }

    public static void setLanguagesSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getLanguages() == null) {
            activity.setLanguagesSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
        } else {
            for (String language : activity.getVn().getLanguages()) {
                data.add(new VNDetailsElement.Data().setImage1(Language.FLAGS.get(language)).setText1(Language.FULL_TEXT.get(language)));
            }

            if (activity.getLanguagesSubmenu() == null) {
                activity.setLanguagesSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getLanguagesSubmenu().setData(data);
            }
        }
    }

    public static void setPlatformsSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getPlatforms() == null) {
            activity.setPlatformsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
        } else {
            for (String platform : activity.getVn().getPlatforms()) {
                data.add(new VNDetailsElement.Data().setImage1(Platform.IMAGES.get(platform)).setText1(Platform.FULL_TEXT.get(platform)));
            }

            if (activity.getPlatformsSubmenu() == null) {
                activity.setPlatformsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getPlatformsSubmenu().setData(data);
            }
        }
    }

    public static void setAnimesSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getAnime() == null) {
            activity.setAnimesSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (Anime anime : activity.getVn().getAnime()) {
                String title_kanji = anime.getTitle_kanji() != null ? anime.getTitle_kanji() + "\n" : "";
                String type = anime.getType() != null ? anime.getType() + activity.getString(R.string.bullet) : "";
                String year = anime.getYear() > 0 ? anime.getYear() + "" : "";

                data.add(new VNDetailsElement.Data()
                        .setText1(anime.getTitle_romaji())
                        .setText2(title_kanji + type + year)
                        .setId(anime.getId()));
            }

            if (activity.getAnimesSubmenu() == null) {
                activity.setAnimesSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getAnimesSubmenu().setData(data);
            }
        }
    }

    public static void setRelationsSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getRelations() == null) {
            activity.setRelationsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (Relation relation : activity.getVn().getRelations()) {
                data.add(new VNDetailsElement.Data()
                        .setText1(relation.getTitle())
                        .setText2(Relation.TYPES.get(relation.getRelation()))
                        .setId(relation.getId()));
            }

            if (activity.getRelationsSubmenu() == null) {
                activity.setRelationsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getRelationsSubmenu().setData(data);
            }
        }
    }

    public static void setSimilarNovelsSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getSimilarNovels() == null) {
            activity.setSimilarNovelsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (SimilarNovel similarNovel : activity.getSimilarNovels()) {
                data.add(new VNDetailsElement.Data()
                        .setId(similarNovel.getNovelId())
                        .setText1(similarNovel.getTitle())
                        .setText2("Similarity : " + similarNovel.getSimilarityPercentage() + "%")
                        .setUrlImage(similarNovel.getImageLink())
                        .setImage2(similarNovel.getSimilarityImage())
                );
            }

            if (activity.getSimilarNovelsSubmenu() == null) {
                activity.setSimilarNovelsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getSimilarNovelsSubmenu().setData(data);
            }
        }
    }

    public static void setTagsSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getTags() == null) {
            activity.setTagsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
        } else {
            Map<String, Boolean> alreadyProcessedCategories = new HashMap<>();
            for (List<Number> catInfo : activity.getVn().getTags()) {
                if (!Tag.checkSpoilerLevel(activity, catInfo.get(2).intValue())) continue;
                Tag cat = Tag.getTags(activity).get(catInfo.get(0).intValue());
                if (cat != null && alreadyProcessedCategories.get(cat.getCat()) == null) {
                    alreadyProcessedCategories.put(cat.getCat(), true);
                    data.add(new VNDetailsElement.Data().setText1("<b>" + Category.CATEGORIES.get(cat.getCat()) + " :</b>"));

                    for (List<Number> tagInfo : activity.getVn().getTags()) {
                        if (!Tag.checkSpoilerLevel(activity, tagInfo.get(2).intValue())) continue;
                        Tag tag = Tag.getTags(activity).get(tagInfo.get(0).intValue());
                        if (tag != null && tag.getCat().equals(cat.getCat())) {
                            data.add(new VNDetailsElement.Data().setText1(tag.getName()).setImage1(Tag.getScoreImage(tagInfo)).setId(tag.getId()));
                        }
                    }
                }
            }

            if (activity.getTagsSubmenu() == null) {
                activity.setTagsSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getTagsSubmenu().setData(data);
            }
        }
    }

    public static void setGenresSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getTags() == null) {
            activity.setGenresSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
        } else {
            for (List<Number> tagInfo : activity.getVn().getTags()) {
                if (!Tag.checkSpoilerLevel(activity, tagInfo.get(2).intValue())) continue;
                Tag tag = Tag.getTags(activity).get(tagInfo.get(0).intValue());
                if (tag != null && Genre.contains(tag.getName())) {
                    data.add(new VNDetailsElement.Data().setText1(tag.getName()));
                }
            }

            if (activity.getGenresSubmenu() == null) {
                activity.setGenresSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_TEXT));
            } else {
                activity.getGenresSubmenu().setData(data);
            }
        }
    }

    public static void setScreensSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getScreens() == null) {
            activity.setScreensSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_IMAGES));
        } else {
            for (Screen screenshot : activity.getVn().getScreens()) {
                if (screenshot.isNsfw() && activity.nsfwLevel <= 0) continue;
                data.add(new VNDetailsElement.Data().setText1(screenshot.getImage()));
            }

            if (activity.getScreensSubmenu() == null) {
                activity.setScreensSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_IMAGES));
            } else {
                activity.getScreensSubmenu().setData(data);
            }
        }
    }

    public static void setCharactersSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();
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
            activity.setCharactersSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
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

                data.add(new VNDetailsElement.Data()
                        .setText1(character.getName())
                        .setText2(Item.ROLES.get(character.getVns().get(0)[Item.ROLE_INDEX].toString()))
                        .setUrlImage(character.getImage())
                        .setId(character.getId())
                        .setButton(R.drawable.ic_record_voice_over_white_48dp)
                );
            }

            if (activity.getCharactersSubmenu() == null) {
                activity.setCharactersSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getCharactersSubmenu().setData(data);
            }
        }
    }

    public static void setStaffSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getVn().getStaff() == null) {
            activity.setStaffSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            Map<String, Boolean> alreadyProcessedRoles = new HashMap<>();
            for (StaffSummary staff : activity.getVn().getStaff()) {
                String role = staff.getRole();
                if (role != null && alreadyProcessedRoles.get(role) == null) {
                    alreadyProcessedRoles.put(role, true);
                    data.add(new VNDetailsElement.Data().setText1("<b>" + Category.CATEGORIES.get(role) + " :</b>"));

                    for (StaffSummary staffInfo : activity.getVn().getStaff()) {
                        if (staffInfo != null && staffInfo.getRole().equals(role)) {
                            data.add(new VNDetailsElement.Data()
                                    .setText1(staffInfo.getName())
                                    .setText2(staffInfo.getNote())
                                    .setImage1(staffInfo.getIcon())
                                    .setId(staffInfo.getId())
                            );
                        }
                    }
                }
            }

            if (activity.getStaffSubmenu() == null) {
                activity.setStaffSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
            } else {
                activity.getStaffSubmenu().setData(data);
            }
        }
    }

    public static void setReleasesSubmenu(VNDetailsActivity activity) {
        List<VNDetailsElement.Data> data = new ArrayList<>();

        if (activity.getReleases() == null) {
            activity.setReleasesSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
        } else {
            for (String language : activity.getReleases().keySet()) {
                data.add(new VNDetailsElement.Data()
                        .setText1("<b>" + Language.FULL_TEXT.get(language) + " :</b>")
                        .setImage1(Language.FLAGS.get(language)));

                for (Item release : activity.getReleases().get(language)) {
                    data.add(new VNDetailsElement.Data()
                            .setText1(release.getTitle())
                            .setText2(Utils.getDate(release.getReleased(), true) + activity.getString(R.string.bullet) + Utils.capitalize(release.getType()))
                            .setId(release.getId())
                    );
                }

                if (activity.getReleasesSubmenu() == null) {
                    activity.setReleasesSubmenu(new VNDetailsElement(data, VNDetailsElement.TYPE_SUBTITLE));
                } else {
                    activity.getReleasesSubmenu().setData(data);
                }
            }
        }
    }
}