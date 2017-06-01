package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.MainActivity;
import com.booboot.vndbandroid.adapter.vncards.Card;
import com.booboot.vndbandroid.adapter.vncards.VNCardsAdapter;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndbandroid.Priority;
import com.booboot.vndbandroid.model.vndbandroid.Status;
import com.booboot.vndbandroid.model.vndbandroid.Vote;
import com.booboot.vndbandroid.util.GridAutofitLayoutManager;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.booboot.vndbandroid.util.image.Pixels;

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
            subtitle.append(vn.getRating()).append(" (").append(Vote.INSTANCE.getName(vn.getRating())).append(")");
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
        else
            card.setImageUrl(vn.getImage());

        if (Cache.vnlist.get(vn.getId()) != null)
            card.setStatus(Status.INSTANCE.toShortString(Cache.vnlist.get(vn.getId()).getStatus()));
        else card.setStatus(activity.getString(R.string.dash));

        if (Cache.wishlist.get(vn.getId()) != null)
            card.setWish(Priority.INSTANCE.toShortString(Cache.wishlist.get(vn.getId()).getPriority()));
        else card.setWish(activity.getString(R.string.dash));

        if (Cache.votelist.get(vn.getId()) != null)
            card.setVote(Vote.INSTANCE.toShortString(Cache.votelist.get(vn.getId()).getVote()) + "");
        else card.setVote(activity.getString(R.string.dash));

        ((VNCardsAdapter) materialListView.getAdapter()).addCard(card);
    }

    public static void setupList(final Context context, RecyclerView materialListView) {
        materialListView.setLayoutManager(new GridAutofitLayoutManager(context, Pixels.px(300, context)));
        materialListView.setAdapter(new VNCardsAdapter(context));
        if (context instanceof MainActivity) {
            /* Automatically hide the FAB button when reaching the bottom of the RecyclerView (only in MainActivity of course) */
            materialListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                        int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                        ((MainActivity) context).toggleFloatingSearchButton(visibleItemCount + pastVisiblesItems < totalItemCount);
                    } else {
                        ((MainActivity) context).toggleFloatingSearchButton(true);
                    }
                }
            });
        }
    }
}
