package com.booboot.vndbandroid.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.search.SearchOptionsAdapter;
import com.booboot.vndbandroid.adapter.search.TagFilteredArrayAdapter;
import com.booboot.vndbandroid.bean.Options;
import com.booboot.vndbandroid.bean.Tag;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.booboot.vndbandroid.view.TagAutoCompleteView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tokenautocomplete.TokenCompleteTextView;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VNSearchActivity extends AppCompatActivity {
    public final static String SAVED_QUERY_STATE = "SAVED_QUERY_STATE";
    public final static String INCLUDE_TAGS_STATE = "INCLUDE_TAGS_STATE";
    public final static String EXCLUDE_TAGS_STATE = "EXCLUDE_TAGS_STATE";
    public final static String INCLUDE_TAGS_HINT = "INCLUDE_TAGS_HINT";
    public final static String SEARCH_OPTIONS_VISIBILITY = "SEARCH_OPTIONS_VISIBILITY";
    public final static String INCLUDE_TAGS = "INCLUDE_TAGS";
    public final static String EXCLUDE_TAGS = "EXCLUDE_TAGS";

    private Bundle savedInstanceState;
    private ProgressiveResultLoader progressiveResultLoader;
    private SearchView searchView;
    private String savedQuery;

    private SearchOptionsAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    public LinearLayout searchOptionsLayout;

    private TagAutoCompleteView includeTagsInput;
    private TagAutoCompleteView excludeTagsInput;
    private Set<Integer> includeTags = new HashSet<>();
    private Set<Integer> excludeTags = new HashSet<>();
    private FloatLabeledEditText includeTagsFloatingLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.vn_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressiveResultLoader = new ProgressiveResultLoader();
        progressiveResultLoader.setActivity(this);
        progressiveResultLoader.setOptions(new Options());
        progressiveResultLoader.init();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListAdapter = new SearchOptionsAdapter(this);
        expandableListView.setAdapter(expandableListAdapter);
        searchOptionsLayout = (LinearLayout) findViewById(R.id.searchOptionsLayout);

        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
            savedQuery = savedInstanceState.getString(SAVED_QUERY_STATE);
            //noinspection WrongConstant
            searchOptionsLayout.setVisibility(savedInstanceState.getInt(SEARCH_OPTIONS_VISIBILITY));
            ArrayList<Integer> savedTags = savedInstanceState.getIntegerArrayList(INCLUDE_TAGS);
            if (savedTags != null) includeTags = new HashSet<>(savedTags);
            savedTags = savedInstanceState.getIntegerArrayList(EXCLUDE_TAGS);
            if (savedTags != null) excludeTags = new HashSet<>(savedTags);
        }

        initSearchOptions();
    }

    private void initSearchOptions() {
        includeTagsInput = (TagAutoCompleteView) findViewById(R.id.includeTagsInput);
        excludeTagsInput = (TagAutoCompleteView) findViewById(R.id.excludeTagsInput);
        includeTagsFloatingLabel = (FloatLabeledEditText) findViewById(R.id.includeTagsFloatingLabel);
        final ImageView includeTagsIcon = (ImageView) findViewById(R.id.includeTagsIcon);
        final ImageView includeTagsDropdown = (ImageView) findViewById(R.id.includeTagsDropdown);
        ImageView excludeTagsIcon = (ImageView) findViewById(R.id.excludeTagsIcon);

        initCompletionView(includeTagsInput, VNSearchActivity.INCLUDE_TAGS_STATE, includeTags);
        initCompletionView(excludeTagsInput, VNSearchActivity.EXCLUDE_TAGS_STATE, excludeTags);

        Utils.tintImage(this, includeTagsIcon, R.color.green, false);
        Utils.tintImage(this, includeTagsDropdown, R.color.green, false);
        Utils.tintImage(this, excludeTagsIcon, R.color.red, false);

        LinearLayout includeTagsLayout = (LinearLayout) findViewById(R.id.includeTagsLayout);
        assert includeTagsLayout != null;
        includeTagsLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        includeTagsIcon.setAlpha(0.9f);
                        includeTagsDropdown.setAlpha(0.9f);
                        break;

                    case MotionEvent.ACTION_UP:
                        includeTagsIcon.setAlpha(0.4f);
                        includeTagsDropdown.setAlpha(0.4f);
                        PopupMenu popup = new PopupMenu(VNSearchActivity.this, includeTagsDropdown);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.include_tags, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.item_include_all) {
                                    includeTagsInput.setHint(R.string.include_all_tags);
                                    includeTagsFloatingLabel.setHint(getResources().getString(R.string.include_all_tags));
                                } else if (item.getItemId() == R.id.item_include_one) {
                                    includeTagsInput.setHint(R.string.include_one_tags);
                                    includeTagsFloatingLabel.setHint(getResources().getString(R.string.include_one_tags));
                                }
                                return false;
                            }
                        });
                        popup.show();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        includeTagsIcon.setAlpha(0.4f);
                        includeTagsDropdown.setAlpha(0.4f);
                        break;
                }
                return true;
            }
        });
    }

    private void initCompletionView(final TagAutoCompleteView tagsInput, final String tagsState, final Set<Integer> ids) {
        tagsInput.allowCollapse(false);
        tagsInput.allowDuplicates(false);
        tagsInput.performBestGuess(false);
        tagsInput.setThreshold(1);
        ArrayAdapter<Tag> adapter = new TagFilteredArrayAdapter(this, R.layout.token_autocomplete_list, Tag.getTagsArray(this), tagsInput);
        tagsInput.setAdapter(adapter);
        tagsInput.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);

        tagsInput.setTokenListener(new TokenCompleteTextView.TokenListener<Tag>() {
            @Override
            public void onTokenAdded(Tag token) {
                ids.add(token.getId());
            }

            @Override
            public void onTokenRemoved(Tag token) {
                ids.remove(token.getId());
            }
        });

        if (savedInstanceState != null) {
            Parcelable savedState = savedInstanceState.getParcelable(tagsState);
            if (savedState != null) {
                tagsInput.onRestoreInstanceState(savedState);
                CharSequence hint = savedInstanceState.getCharSequence(INCLUDE_TAGS_HINT);
                if (hint != null) {
                    tagsInput.setHint(hint);
                    includeTagsFloatingLabel.setHint(hint.toString());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
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
                return submitSearch(query);
            }

            @Override
            public boolean onQueryTextChange(String search) {
                return false;
            }
        });

        if (savedQuery != null) searchView.setQuery(savedQuery, true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_send_search:
                submitSearch(searchView.getQuery().toString());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean submitSearch(String query) {
        searchView.clearFocus();
        progressiveResultLoader.setCurrentPage(1);
        query = query.trim();
        List<String> filters = new ArrayList<>();
        try {
            if (query.length() > 0)
                filters.add("search ~ \"" + query.trim() + "\"");
            if (!includeTags.isEmpty()) {
                if (includeTagsInput.getHint().toString().equals(getResources().getString(R.string.include_one_tags))) {
                    filters.add("tags = " + JSON.mapper.writeValueAsString(includeTags));
                } else {
                    filters.add("tags = " + TextUtils.join(" and tags = ", includeTags));
                }
            }
            if (!excludeTags.isEmpty()) {
                filters.add("tags != " + JSON.mapper.writeValueAsString(excludeTags));
            }
        } catch (JsonProcessingException e) {
            Callback.showToast(this, "An error occurred while creating your search. Please try again later.");
            return false;
        }

        if (filters.isEmpty()) {
            Callback.showToast(this, "Your search is empty.");
            return false;
        }

        progressiveResultLoader.setFilters("(" + TextUtils.join(" and ", filters) + ")");
        progressiveResultLoader.loadResults(true);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (searchView != null)
            savedInstanceState.putString(SAVED_QUERY_STATE, searchView.getQuery().toString());
        if (expandableListAdapter != null && includeTagsInput != null) {
            savedInstanceState.putParcelable(INCLUDE_TAGS_STATE, includeTagsInput.onSaveInstanceState());
            savedInstanceState.putParcelable(EXCLUDE_TAGS_STATE, excludeTagsInput.onSaveInstanceState());
            savedInstanceState.putCharSequence(INCLUDE_TAGS_HINT, includeTagsInput.getHint());
        }
        savedInstanceState.putInt(SEARCH_OPTIONS_VISIBILITY, searchOptionsLayout.getVisibility());
        savedInstanceState.putIntegerArrayList(INCLUDE_TAGS, new ArrayList<>(includeTags));
        savedInstanceState.putIntegerArrayList(EXCLUDE_TAGS, new ArrayList<>(excludeTags));

        super.onSaveInstanceState(savedInstanceState);
    }
}
