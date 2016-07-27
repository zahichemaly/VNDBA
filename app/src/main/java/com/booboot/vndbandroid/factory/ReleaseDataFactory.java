package com.booboot.vndbandroid.factory;

import android.text.TextUtils;

import com.booboot.vndbandroid.adapter.doublelist.DoubleListElement;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndbandroid.Language;
import com.booboot.vndbandroid.bean.vndb.Media;
import com.booboot.vndbandroid.bean.vndbandroid.Platform;
import com.booboot.vndbandroid.bean.vndb.Producer;
import com.booboot.vndbandroid.util.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by od on 14/04/2016.
 */
public class ReleaseDataFactory {
    /**
     * @param release release we want to display the info
     * @return DoubleListElement[] array to be given to a DoubleListAdapter
     */
    public static DoubleListElement[] getData(Item release) {
        final List<DoubleListElement> releaseData = new ArrayList<>();
        if (release.getNotes() != null)
            releaseData.add(new DoubleListElement("Notes", release.getNotes(), true));
        if (release.getOriginal() != null)
            releaseData.add(new DoubleListElement("Original name", release.getOriginal(), false));
        if (release.getReleased() != null)
            releaseData.add(new DoubleListElement("Released date", Utils.getDate(release.getReleased(), true), false));
        if (release.getType() != null)
            releaseData.add(new DoubleListElement("Type", Utils.capitalize(release.getType()), false));

        List<String> publication = new ArrayList<>();
        if (release.isFreeware()) publication.add("Freeware");
        if (release.isPatch()) publication.add("Patch");
        if (release.isDoujin()) publication.add("Doujin");
        releaseData.add(new DoubleListElement("Publication", TextUtils.join(", ", publication), false));

        List<String> full_languages = new ArrayList<>();
        for (String language : release.getLanguages()) {
            full_languages.add(Language.FULL_TEXT.get(language));
        }
        releaseData.add(new DoubleListElement("Languages", TextUtils.join(", ", full_languages), false));

        List<String> full_platforms = new ArrayList<>();
        for (String platform : release.getPlatforms()) {
            full_platforms.add(Platform.FULL_TEXT.get(platform));
        }
        releaseData.add(new DoubleListElement("Platforms", TextUtils.join(", ", full_platforms), false));

        List<String> media = new ArrayList<>();
        for (Media medium : release.getMedia()) {
            media.add(Media.FULL_TEXT.get(medium.getMedium()) + (medium.getQty() > 0 ? " (" + medium.getQty() + ")" : ""));
        }
        releaseData.add(new DoubleListElement("Medium", TextUtils.join(", ", media), false));

        if (release.getWebsite() != null)
            releaseData.add(new DoubleListElement("Website", "<a href=\"" + release.getWebsite() + "\">" + release.getWebsite() + "</a>", false));
        releaseData.add(new DoubleListElement("Age rating", release.getMinage() > 0 ? release.getMinage() + "+" : "Everyone", false));
        if (release.getGtin() != null)
            releaseData.add(new DoubleListElement("JAN/UPC/EAN", release.getGtin(), false));
        if (release.getCatalog() != null)
            releaseData.add(new DoubleListElement("Catalog n°", release.getCatalog(), false));

        LinkedHashMap<String, List<String>> producers = new LinkedHashMap<>();
        producers.put("Developer", new ArrayList<String>());
        producers.put("Publisher", new ArrayList<String>());
        for (Producer producer : release.getProducers()) {
            if (producer.isDeveloper())
                producers.get("Developer").add(producer.getName());
            if (producer.isPublisher())
                producers.get("Publisher").add(producer.getName());
        }
        for (String producer : producers.keySet()) {
            if (producers.get(producer).isEmpty()) continue;
            releaseData.add(new DoubleListElement(producer, TextUtils.join(", ", producers.get(producer)), false));
        }

        return releaseData.toArray(new DoubleListElement[releaseData.size()]);
    }
}
