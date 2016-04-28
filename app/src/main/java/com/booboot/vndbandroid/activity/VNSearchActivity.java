package com.booboot.vndbandroid.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.db.Cache;
import com.booboot.vndbandroid.factory.VNCardFactory;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;

public class VNSearchActivity extends AppCompatActivity {
    private MaterialListView materialListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.vn_card_list_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        materialListView = (MaterialListView) findViewById(R.id.materialListView);
        /* [Fix] Set the background color to match the default one (not the case by default) */
        materialListView.getRootView().setBackgroundColor(getResources().getColor(R.color.windowBackground, getTheme()));

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Little trick to put the icon inside the input field and prevent the search view from collapsing when clicking on "x"
        searchView.setIconified(false);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });
        searchView.requestFocus();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                VNDBServer.get("vn", Cache.VN_FLAGS, "(search ~ \"" + query.trim() + "\")", Options.create(false, false), VNSearchActivity.this, new Callback() {
                    @Override
                    protected void config() {
                        materialListView.getAdapter().clearAll();

                        for (final Item vn : results.getItems()) {
                            VNCardFactory.buildCard(VNSearchActivity.this, vn, materialListView);
                        }

                        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(Card card, int position) {
                                Intent intent = new Intent(VNSearchActivity.this, VNDetailsActivity.class);
                                intent.putExtra(VNTypeFragment.VN_ARG, (Item) card.getTag());
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(Card card, int position) {
                                Log.d("LONG_CLICK", card.getProvider().getTitle());
                            }
                        });
                    }
                }, Callback.errorCallback(VNSearchActivity.this));

                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        VNDetailsActivity.spoilerLevel = -1;
        super.onResume();
    }
}
