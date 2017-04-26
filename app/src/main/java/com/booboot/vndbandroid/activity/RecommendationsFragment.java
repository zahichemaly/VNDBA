package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vncards.RecyclerItemClickListener;
import com.booboot.vndbandroid.adapter.vncards.VNCardsListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.DB;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.VNStatServer;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndb.Options;
import com.booboot.vndbandroid.model.vnstat.SimilarNovel;
import com.booboot.vndbandroid.model.vnstat.VNStatItem;
import com.booboot.vndbandroid.factory.FastScrollerFactory;
import com.booboot.vndbandroid.factory.PopupMenuFactory;
import com.booboot.vndbandroid.factory.VNCardFactory;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by od on 13/03/2016.
 */
public class RecommendationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View rootView;
    public static List<SimilarNovel> recommendations;
    private PopupWindow optionsPopup;
    private VNCardsListView materialListView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vn_card_list, container, false);
        Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_recommendations));

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        materialListView = (VNCardsListView) rootView.findViewById(R.id.materialListView);
        VNCardFactory.setupList(getActivity(), materialListView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            materialListView.getRootView().setBackgroundColor(rootView.getResources().getColor(R.color.windowBackground, rootView.getContext().getTheme()));
        } else {
            materialListView.getRootView().setBackgroundColor(rootView.getResources().getColor(R.color.windowBackground));
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardView cardView, int position) {
                Cache.openVNDetails(getActivity(), (int) cardView.getTag());
            }
        }));

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(getActivity(), R.attr.colorAccent));
        FastScrollerFactory.get(getActivity(), rootView, materialListView, refreshLayout);

        refresh(false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.recommendations, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_recommendations_options:
                optionsPopup = PopupMenuFactory.get(getActivity(), R.layout.recommendations_menu, getActivity().findViewById(R.id.action_recommendations_options), optionsPopup, new PopupMenuFactory.Callback() {
                    @Override
                    public void create(View content) {
                        CheckBox checkHideInWishlist = (CheckBox) content.findViewById(R.id.check_hide_in_wishlist);
                        checkHideInWishlist.setOnClickListener(RecommendationsFragment.this);
                        checkHideInWishlist.setChecked(SettingsManager.getHideRecommendationsInWishlist(getActivity()));

                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_hide_in_wishlist:
                SettingsManager.setHideRecommendationsInWishlist(getActivity(), ((CheckBox) view).isChecked());
                refresh(false);
                break;
        }
    }

    public void refresh(final boolean forceRefresh) {
        progressBar.setVisibility(View.VISIBLE);
        final Callback successCallback = new Callback() {
            @Override
            protected void config() {
                recommendations = vnStatResults.getRecommendations();
                Collections.sort(recommendations, new Comparator<SimilarNovel>() {
                            @Override
                            public int compare(SimilarNovel a, SimilarNovel b) {
                                return Double.valueOf(b.getPredictedRating()).compareTo(a.getPredictedRating());
                            }
                        }
                );

                DB.saveRecommendations(getActivity(), recommendations);

                if (recommendations == null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    refreshLayout.setRefreshing(false);
                    return;
                }

                displayRecommendations();
            }
        };

        if (recommendations != null && !forceRefresh) {
            displayRecommendations();
            return;
        }

        int userId = SettingsManager.getUserId(getActivity());
        if (userId < 0) {
            VNDBServer.get("user", "basic", "(id = 0)", Options.create(false, 1), 0, getActivity(), new Callback() {
                @Override
                protected void config() {
                    if (results.getItems().size() > 0) {
                        int userId = results.getItems().get(0).getId();
                        SettingsManager.setUserId(getActivity(), userId);
                        fetchRecommendations(userId, successCallback, forceRefresh);
                    } else {
                        Callback.showToast(getActivity(), "No user ID has been found to fetch your recommendations.");
                    }
                }
            }, new Callback() {
                @Override
                protected void config() {
                    showToast(getActivity(), message);
                    progressBar.setVisibility(View.INVISIBLE);
                    refreshLayout.setRefreshing(false);
                }
            });
        } else {
            fetchRecommendations(userId, successCallback, forceRefresh);
        }
    }

    public void fetchRecommendations(int userId, final Callback successCallback, final boolean forceRefresh) {
        VNStatServer.get("user", "recommendations", userId, successCallback, new Callback() {
                    @Override
                    protected void config() {
                        recommendations = DB.loadRecommendations(getActivity());
                        if (recommendations.size() > 0 && !forceRefresh) {
                            successCallback.vnStatResults = new VNStatItem();
                            successCallback.vnStatResults.setRecommendations(recommendations);
                            successCallback.call();
                        } else {
                            if (message.startsWith("User ID does not exist")) {
                                showBackgroundInfo(getActivity().getString(R.string.recommendations_private_warning), true);
                                recommendations = null;
                            } else {
                                showToast(getActivity(), message);
                            }
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        refreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void showBackgroundInfo(String text, boolean showToast) {
        if (recommendations.size() > 0) {
            rootView.findViewById(R.id.backgroundInfo).setVisibility(View.GONE);
            /* If the lists were public, but the user made them private in the meantime, we can show recommendations BUT have to tell the user "these recommendations cannot be updated" */
            if (showToast) Callback.showToast(getActivity(), text);
        } else {
            final ImageView backgroundInfoImage = (ImageView) rootView.findViewById(R.id.backgroundInfoImage);
            Utils.tintImage(getActivity(), backgroundInfoImage, android.R.color.darker_gray, false);
            final TextView backgroundInfoText = (TextView) rootView.findViewById(R.id.backgroundInfoText);
            backgroundInfoText.setText(text);
            rootView.findViewById(R.id.backgroundInfo).setVisibility(View.VISIBLE);
        }
    }

    public void displayRecommendations() {
        materialListView.getAdapter().clearAll();
        showBackgroundInfo("You don't have enough novels in your list so we can give you recommendations. Add more novels and come back here later!", false);

        for (SimilarNovel recommendation : recommendations) {
            if (Cache.wishlist != null && Cache.wishlist.containsKey(recommendation.getNovelId()) && SettingsManager.getHideRecommendationsInWishlist(getActivity()))
                continue; // Recommendation already in wishlist: don't show it

            Item vn = new Item(recommendation.getNovelId());
            vn.setPopularity(recommendation.getPredictedRatingPercentage());
            vn.setTitle(recommendation.getTitle());
            vn.setLength(-1);
            vn.setImage(recommendation.getImageLink());
            vn.setReleased(recommendation.getReleased());
            VNCardFactory.buildCard(getActivity(), vn, materialListView, false, true, false, true, false);
        }

        progressBar.setVisibility(View.INVISIBLE);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        refresh(true);
    }
}
