package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vncards.Card;
import com.booboot.vndbandroid.adapter.vncards.RecyclerItemClickListener;
import com.booboot.vndbandroid.adapter.vncards.VNCardsListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndb.Options;
import com.booboot.vndbandroid.model.vndbandroid.ProgressiveResultLoaderOptions;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 13/05/2016.
 */
public class ProgressiveResultLoader implements SwipeRefreshLayout.OnRefreshListener {
    private Activity activity;
    private View rootView;
    private VNCardsListView materialListView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    private int currentPage = 1;
    private boolean moreResults;

    private String filters;
    private Options options;
    private boolean showFullDate;
    private boolean showRank;
    private boolean showRating;
    private boolean showPopularity;
    private boolean showVoteCount;

    public void init() {
        progressBar = (ProgressBar) (rootView == null ? activity.findViewById(R.id.progressBar) : rootView.findViewById(R.id.progressBar));
        materialListView = (VNCardsListView) (rootView == null ? activity.findViewById(R.id.materialListView) : rootView.findViewById(R.id.materialListView));
        VNCardFactory.setupList(activity, materialListView);

        /* [Fix] Set the background color to match the default one (not the case by default) */
        if (rootView == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                materialListView.getRootView().setBackgroundColor(activity.getResources().getColor(R.color.windowBackground, activity.getTheme()));
            } else {
                materialListView.getRootView().setBackgroundColor(activity.getResources().getColor(R.color.windowBackground));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                materialListView.getRootView().setBackgroundColor(rootView.getResources().getColor(R.color.windowBackground, rootView.getContext().getTheme()));
            } else {
                materialListView.getRootView().setBackgroundColor(rootView.getResources().getColor(R.color.windowBackground));
            }
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardView cardView, int position) {
                Cache.openVNDetails(activity, (int) cardView.getTag());
            }
        }));

        materialListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && !progressBar.isShown() && moreResults) {
                    int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int pastVisiblesItems = 0;
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        /* Computing past visible items in portrait (1 column) */
                        pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    } else {
                        /* Computing past visible items in landscape (> 1 column) */
                        int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null);
                        for (int i = 0; i < firstVisibleItemPositions.length - 1; i++)
                            pastVisiblesItems += firstVisibleItemPositions[i];
                    }

                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        currentPage++;
                        loadResults(false);
                    }
                }
            }
        });

        refreshLayout = (SwipeRefreshLayout) (rootView == null ? activity.findViewById(R.id.refreshLayout) : rootView.findViewById(R.id.refreshLayout));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(activity, R.attr.colorAccent));

        FastScrollerFactory.get(activity, rootView, materialListView, refreshLayout);
    }

    public void loadResults(final boolean clearData) {
        options.setPage(currentPage);
        progressBar.setVisibility(View.VISIBLE);
        VNDBServer.get("vn", Cache.VN_FLAGS, filters, options, 0, activity, new Callback() {
            @Override
            protected void config() {
                moreResults = results.isMore();
                if (clearData)
                    materialListView.getAdapter().clearAll();

                for (final Item vn : results.getItems()) {
                    VNCardFactory.buildCard(activity, vn, materialListView, showFullDate, showRank, showRating, showPopularity, showVoteCount);
                    if (!Cache.vns.containsKey(vn.getId())) Cache.vns.put(vn.getId(), vn);
                }

                /* Scrolling to top only when we fetch the first results page and if there are results (otherwise the scroll is messed up) */
                if (currentPage == 1 && results.getItems().size() > 0)
                    materialListView.scrollToPosition(0);
                progressBar.setVisibility(View.INVISIBLE);
                refreshLayout.setRefreshing(false);
            }
        }, new Callback() {
            @Override
            public void config() {
                progressBar.setVisibility(View.INVISIBLE);
                refreshLayout.setRefreshing(false);
                Callback.showToast(activity, message);
            }
        });
    }

    public void initFromExisting(ProgressiveResultLoaderOptions options) {
        init();
        currentPage = options.getCurrentPage();
        moreResults = options.isMoreResults();
        for (Card card : options.getCards()) {
            if (card == null) continue;
            int vnId = card.getVnId();
            Item vn = Cache.vns.get(vnId);
            VNCardFactory.buildCard(activity, vn, materialListView, showFullDate, showRank, showRating, showPopularity, showVoteCount);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadResults(true);
    }

    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < materialListView.getAdapter().getItemCount(); i++) {
            cards.add(materialListView.getAdapter().getCard(i));
        }
        return cards;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public void setMaterialListView(VNCardsListView materialListView) {
        this.materialListView = materialListView;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public void setMoreResults(boolean moreResults) {
        this.moreResults = moreResults;
    }

    public void setShowFullDate(boolean showFullDate) {
        this.showFullDate = showFullDate;
    }

    public void setShowRank(boolean showRank) {
        this.showRank = showRank;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
    }

    public void setShowPopularity(boolean showPopularity) {
        this.showPopularity = showPopularity;
    }

    public void setShowVoteCount(boolean showVoteCount) {
        this.showVoteCount = showVoteCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isMoreResults() {
        return moreResults;
    }
}