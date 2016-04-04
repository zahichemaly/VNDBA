package com.booboot.vndbandroid.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.util.Callback;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;

public class VNSearchActivity extends AppCompatActivity {
    private MaterialListView materialListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vn_card_list_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        materialListView = (MaterialListView) findViewById(R.id.materialListView);
        /* [Fix] Set the background color to match the default one (not the case by default) */
        materialListView.getRootView().setBackgroundColor(getResources().getColor(R.color.windowBackground, getTheme()));
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
                VNDBServer.get("vn", "basic,details,screens,tags,stats", "(search ~ \"" + query.trim() + "\")", Options.create(1, 25, null, false), VNSearchActivity.this, new Callback() {
                    @Override
                    protected void config() {
                        materialListView.getAdapter().clearAll();

                        Log.d("D", results.getItems().size() + "");
                        for (final Item vn : results.getItems()) {
                            Card card = new Card.Builder(VNSearchActivity.this)
                                    .withProvider(new CardProvider())
                                    .setLayout(R.layout.vn_card_layout)
                                    .setTitle(vn.getTitle())
                                    .setSubtitle(vn.getOriginal())
                                    .setSubtitleColor(Color.BLACK)
                                    .setTitleGravity(Gravity.END)
                                    .setSubtitleGravity(Gravity.END)
                                    .setDescription(vn.getReleased())
                                    .setDescriptionGravity(Gravity.END)
                                    .setDrawable(vn.getImage())
                                    .endConfig().build();
                            card.setTag(vn);

                            materialListView.getAdapter().add(card);
                            materialListView.scrollToPosition(0);
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
}
