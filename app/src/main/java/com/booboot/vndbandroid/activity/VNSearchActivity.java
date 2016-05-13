package com.booboot.vndbandroid.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;
import com.booboot.vndbandroid.util.SettingsManager;

public class VNSearchActivity extends AppCompatActivity {
    private ProgressiveResultLoader progressiveResultLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.vn_card_list_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressiveResultLoader = new ProgressiveResultLoader();
        progressiveResultLoader.setActivity(this);
        progressiveResultLoader.setOptions(new Options());
        progressiveResultLoader.init();
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
                progressiveResultLoader.setCurrentPage(1);
                progressiveResultLoader.setFilters("(search ~ \"" + query.trim() + "\")");
                progressiveResultLoader.loadResults(true);
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
