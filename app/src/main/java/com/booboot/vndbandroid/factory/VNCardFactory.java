package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vncards.Card;
import com.booboot.vndbandroid.adapter.vncards.VNCardsAdapter;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndbandroid.Priority;
import com.booboot.vndbandroid.bean.vndbandroid.Status;
import com.booboot.vndbandroid.bean.vndbandroid.Vote;
import com.booboot.vndbandroid.util.GridAutofitLayoutManager;
import com.booboot.vndbandroid.util.Pixels;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 17/04/2016.
 */
public class VNCardFactory {
    public static void buildCard(Activity activity, Item vn, RecyclerView materialListView, boolean showFullDate, boolean showRank, boolean showRating, boolean showPopularity, boolean showVoteCount) {
        StringBuilder title = new StringBuilder(), subtitle = new StringBuilder();
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

        Card card = new Card(vn.getId());
        card.setTitle(title.toString());
        card.setSubtitle(subtitle.toString());

        if (vn.isImage_nsfw() && !SettingsManager.getNSFW(activity))
            card.setImageId(R.drawable.ic_nsfw);
        else if (PlaceholderPictureFactory.USE_PLACEHOLDER)
            card.setImageUrl(PlaceholderPictureFactory.getPlaceholderPicture());
        else
            card.setImageUrl(vn.getImage());

        if (Cache.vnlist.get(vn.getId()) != null)
            card.setStatus(Status.toShortString(Cache.vnlist.get(vn.getId()).getStatus()));
        else card.setStatus(activity.getString(R.string.dash));

        if (Cache.wishlist.get(vn.getId()) != null)
            card.setWish(Priority.toShortString(Cache.wishlist.get(vn.getId()).getPriority()));
        else card.setWish(activity.getString(R.string.dash));

        if (Cache.votelist.get(vn.getId()) != null)
            card.setVote(Vote.toShortString(Cache.votelist.get(vn.getId()).getVote()) + "");
        else card.setVote(activity.getString(R.string.dash));

        ((VNCardsAdapter) materialListView.getAdapter()).addCard(card);
    }

    public static void setupList(Context context, RecyclerView materialListView) {
        materialListView.setLayoutManager(new GridAutofitLayoutManager(context, Pixels.px(300, context)));
        materialListView.setAdapter(new VNCardsAdapter(context));
    }
}
