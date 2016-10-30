package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndbandroid.Priority;
import com.booboot.vndbandroid.bean.vndbandroid.Status;
import com.booboot.vndbandroid.bean.vndbandroid.Vote;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;

/**
 * Created by od on 17/04/2016.
 */
public class VNCardFactory {
    public static void buildCard(Activity activity, Item vn, MaterialListView materialListView, boolean showFullDate, boolean showRank, boolean showRating, boolean showPopularity, boolean showVoteCount) {
        StringBuilder title = new StringBuilder(), subtitle = new StringBuilder(), description = new StringBuilder();
        if (showRank)
            title.append("#").append(materialListView.getAdapter().getItemCount() + 1).append(activity.getString(R.string.dash));
        title.append(vn.getTitle());
        if (showRating)
            subtitle.append(vn.getRating()).append(" (").append(Vote.getName(vn.getRating())).append(")");
        else if (showPopularity)
            subtitle.append(vn.getPopularity()).append("%");
        else if (showVoteCount)
            subtitle.append(vn.getVotecount()).append(" votes");
        else
            subtitle.append(Utils.getDate(vn.getReleased(), showFullDate));

        subtitle.append(activity.getString(R.string.bullet));
        if (vn.getLength() > 0)
            subtitle.append(vn.getLengthString());
        else
            subtitle.append(Utils.getDate(vn.getReleased(), true));

        if (Cache.vnlist.get(vn.getId()) != null)
            description.append(Status.toString(Cache.vnlist.get(vn.getId()).getStatus())).append("\n");
        else description.append("Not on your VN list\n");
        if (Cache.wishlist.get(vn.getId()) != null)
            description.append(Priority.toString(Cache.wishlist.get(vn.getId()).getPriority())).append("\n");
        else description.append("Not on your wishlist\n");
        if (Cache.votelist.get(vn.getId()) != null) {
            int vote = Cache.votelist.get(vn.getId()).getVote();
            description.append(Vote.toString(vote));
        } else description.append("Not voted yet");


        CardProvider cardProvider = new Card.Builder(activity)
                .withProvider(new CardProvider())
                .setLayout(R.layout.vn_card_layout)
                .setTitle(title.toString())
                .setSubtitle(subtitle.toString())
                .setSubtitleColor(Color.GRAY)
                .setTitleGravity(Gravity.CENTER)
                .setSubtitleGravity(Gravity.CENTER)
                .setDescription(description.toString())
                .setDescriptionGravity(Gravity.CENTER)
                .setDescriptionColor(Utils.getThemeColor(activity, R.attr.colorPrimaryDark));

        if (vn.isImage_nsfw() && !SettingsManager.getNSFW(activity))
            cardProvider.setDrawable(R.drawable.ic_nsfw);
        else if (PlaceholderPictureFactory.USE_PLACEHOLDER)
            cardProvider.setDrawable(PlaceholderPictureFactory.getPlaceholderPicture());
        else
            cardProvider.setDrawable(vn.getImage());

        Card card = cardProvider.endConfig().build();
        card.setTag(vn.getId());

        materialListView.getAdapter().add(card);
    }

    public static void setCardsPerRow(Activity activity, MaterialListView materialListView) {
        materialListView.setColumnCountLandscape(Utils.isDeviceWide(activity, Utils.LANDSCAPE) ? 3 : Utils.isInMultiWindowMode(activity) ? 1 : 2);
        materialListView.setColumnCountPortrait(Utils.isDeviceWide(activity, Utils.PORTRAIT) ? 2 : 1);
    }
}
