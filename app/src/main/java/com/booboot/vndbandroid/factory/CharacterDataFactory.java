package com.booboot.vndbandroid.factory;

import android.text.TextUtils;

import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListElement;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndb.Tag;
import com.booboot.vndbandroid.bean.vndb.Trait;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by od on 14/04/2016.
 */
public class CharacterDataFactory {
    /**
     * Get all character's info and format them in a DoubleListElement array like so:
     * Gender           F
     * Body             Teen, Slim, Pale
     * Hair             Blond, Long, Spiky Bangs
     * [...]
     * Contains an ~O(n) algorithm that fetches all the character's traits grouped by their parents.
     *
     * @param activity  because Android
     * @param character character we want to display the info and traits
     * @return DoubleListElement[] array to be given to a DoubleListAdapter
     */
    public static DoubleListElement[] getData(VNDetailsActivity activity, Item character) {
        final List<DoubleListElement> characterData = new ArrayList<>();
        if (character.getDescription() != null) {
            String descriptionWithoutSpoilers = character.getDescription();
            if (!Tag.checkSpoilerLevel(activity, 2)) {
                descriptionWithoutSpoilers = descriptionWithoutSpoilers.replaceAll("\\[spoiler\\].*\\[/spoiler\\]", "");
            }
            characterData.add(new DoubleListElement("Description", descriptionWithoutSpoilers, true));
        }
        if (character.getOriginal() != null)
            characterData.add(new DoubleListElement("Original name", character.getOriginal(), false));
        if (character.getGender() != null)
            characterData.add(new DoubleListElement("Gender", character.getGender(), false));
        if (character.getBloodt() != null)
            characterData.add(new DoubleListElement("Blood type", character.getBloodt(), false));
        if (character.getAliases() != null)
            characterData.add(new DoubleListElement("Aliases", character.getAliases(), false));
        if (character.getHeight() > 0)
            characterData.add(new DoubleListElement("Height", character.getHeight() + "cm", false));
        if (character.getWeight() > 0)
            characterData.add(new DoubleListElement("Weight", character.getWeight() + "kg", false));
        if (character.getBust() > 0)
            characterData.add(new DoubleListElement("Bust-Waist-Hips", character.getBust() + "-" + character.getWaist() + "-" + character.getHip() + "cm", false));
        if (character.getBirthday() != null && character.getBirthday()[0] > 0 && character.getBirthday()[1] > 0) {
            Calendar birthday = Calendar.getInstance();
            birthday.set(Calendar.YEAR, 2000);
            birthday.set(Calendar.DAY_OF_MONTH, character.getBirthday()[0]);
            birthday.set(Calendar.MONTH, character.getBirthday()[1]);
            characterData.add(new DoubleListElement("Birthday", new SimpleDateFormat("MMMM d", Locale.US).format(birthday.getTime()), false));
        }

        /* TreeMap to automatically sort traits by id (to display them in the same order as the website) */
        TreeMap<Integer, List<Trait>> characterTraits = new TreeMap<>();
        for (int[] ids : character.getTraits()) {
            int id = ids[0];
            if (!Tag.checkSpoilerLevel(activity, ids[1])) continue;

            Trait trait = Trait.getTraits(activity).get(id);
            if (trait == null) continue;
            Trait rootTrait = trait;
            /* Getting the root element recursively (which will be the name displayed at the left) */
            while (!rootTrait.getParents().isEmpty()) {
                int rootId = rootTrait.getParents().get(0);
                Trait parentTrait = Trait.getTraits(activity).get(rootId);
                if (parentTrait == null) break;
                rootTrait = parentTrait;
            }

            if (characterTraits.get(rootTrait.getId()) == null)
                characterTraits.put(rootTrait.getId(), new ArrayList<Trait>());
            characterTraits.get(rootTrait.getId()).add(trait);
        }

        for (Integer parentId : characterTraits.keySet()) {
            Trait parentTrait = Trait.getTraits(activity).get(parentId);
            characterData.add(new DoubleListElement(parentTrait.getName(), TextUtils.join(", ", characterTraits.get(parentId)), false));
        }

        return characterData.toArray(new DoubleListElement[characterData.size()]);
    }
}
