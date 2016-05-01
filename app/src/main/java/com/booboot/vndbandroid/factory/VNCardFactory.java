package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.MainActivity;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by od on 17/04/2016.
 */
public class VNCardFactory {
    public static void buildCard(Activity activity, Item vn, MaterialListView materialListView) {
        Date released;
        StringBuilder subtitle = new StringBuilder(), description = new StringBuilder();

        if (vn.getReleased() == null) {
            subtitle.append("Unknown");
        } else {
            try {
                released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(vn.getReleased());
                subtitle.append(new SimpleDateFormat("yyyy", Locale.US).format(released));
            } catch (ParseException e) {
                subtitle.append(vn.getReleased());
            }
        }

        subtitle.append(" • ").append(vn.getLengthString());

        if (Cache.vnlist.get(vn.getId()) != null)
            description.append(Status.toString(Cache.vnlist.get(vn.getId()).getStatus())).append("\n");
        else description.append("Not on your VN list\n");
        if (Cache.wishlist.get(vn.getId()) != null)
            description.append(Priority.toString(Cache.wishlist.get(vn.getId()).getPriority())).append("\n");
        else description.append("Not on your wishlist\n");
        if (Cache.votelist.get(vn.getId()) != null) {
            int vote = Cache.votelist.get(vn.getId()).getVote() / 10;
            description.append(vote).append(" (").append(Vote.getName(vote)).append(")\n");
        } else description.append("Not voted yet\n");

        materialListView.setColumnCountLandscape(Utils.isTablet(activity) ? 3 : 2);
        materialListView.setColumnCountPortrait(Utils.isTablet(activity) ? 2 : 1);

        CardProvider cardProvider = new Card.Builder(activity)
                .withProvider(new CardProvider())
                .setLayout(R.layout.vn_card_layout)
                .setTitle(vn.getTitle())
                .setSubtitle(subtitle.toString())
                .setSubtitleColor(Color.GRAY)
                .setTitleGravity(Gravity.CENTER)
                .setSubtitleGravity(Gravity.CENTER)
                .setDescription(description.toString())
                .setDescriptionGravity(Gravity.CENTER)
                .setDescriptionColor(MainActivity.getThemeColor(activity, R.attr.colorPrimaryDark));

        if (vn.isImage_nsfw() && !SettingsManager.getNSFW(activity))
            cardProvider.setDrawable(R.drawable.ic_nsfw);
        else cardProvider.setDrawable(vn.getImage());

        Card card = cardProvider.endConfig().build();
        card.setTag(vn);

        materialListView.getAdapter().add(card);
        materialListView.scrollToPosition(0);
    }
}
