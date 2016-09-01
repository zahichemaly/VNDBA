package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.DB;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.VNStatServer;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndb.Options;
import com.booboot.vndbandroid.bean.vnstat.SimilarNovel;
import com.booboot.vndbandroid.bean.vnstat.VNStatItem;
import com.booboot.vndbandroid.factory.FastScrollerFactory;
import com.booboot.vndbandroid.factory.VNCardFactory;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by od on 13/03/2016.
 */
public class RecommendationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static List<SimilarNovel> recommendations;
    private MaterialListView materialListView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vn_card_list_layout, container, false);
        Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_recommendations));

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        materialListView = (MaterialListView) rootView.findViewById(R.id.materialListView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            materialListView.getRootView().setBackgroundColor(rootView.getResources().getColor(R.color.windowBackground, rootView.getContext().getTheme()));
        } else {
            materialListView.getRootView().setBackgroundColor(rootView.getResources().getColor(R.color.windowBackground));
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(Card card, int position) {
                Cache.openVNDetails(getActivity(), (int) card.getTag());
            }

            @Override
            public void onItemLongClick(Card card, int position) {
            }
        });

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(getActivity(), R.attr.colorAccent));
        FastScrollerFactory.get(getActivity(), rootView, materialListView, refreshLayout);

        refresh(false);
        return rootView;
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
            VNDBServer.get("user", "basic", "(id = 0)", Options.create(false, false, 1), 0, getActivity(), new Callback() {
                @Override
                protected void config() {
                    if (results.getItems().size() > 0) {
                        int userId = results.getItems().get(0).getId();
                        fetchRecommendations(userId, successCallback, forceRefresh);
                    } else {
                        Callback.showToast(getActivity(), "No user ID has been found to fetch your recommendations.");
                    }
                }
            }, Callback.errorCallback(getActivity()));
        } else {
            fetchRecommendations(userId, successCallback, forceRefresh);
        }
    }

    public void fetchRecommendations(int userId, final Callback successCallback, final boolean forceRefresh) {
        VNStatServer.get("user", "recommendations", userId, successCallback, new Callback() {
                    @Override
                    protected void config() {
                        recommendations = DB.loadRecommendations(getActivity());
                        if (recommendations.size() > 0) {
                            successCallback.vnStatResults = new VNStatItem();
                            successCallback.vnStatResults.setRecommendations(recommendations);
                            successCallback.call();
                        }
                        if (recommendations.size() <= 0 || forceRefresh) {
                            showToast(getActivity(), message);
                        }
                    }
                }
        );
    }

    public void displayRecommendations() {
        materialListView.getAdapter().clearAll();
        for (SimilarNovel recommendation : recommendations) {
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
