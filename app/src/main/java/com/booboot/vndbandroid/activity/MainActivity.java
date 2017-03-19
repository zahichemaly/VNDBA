package com.booboot.vndbandroid.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.ranking.RankingMostVotedFragment;
import com.booboot.vndbandroid.activity.ranking.RankingNewlyAddedFragment;
import com.booboot.vndbandroid.activity.ranking.RankingNewlyReleasedFragment;
import com.booboot.vndbandroid.activity.ranking.RankingPopularFragment;
import com.booboot.vndbandroid.activity.ranking.RankingTopFragment;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.model.vndbandroid.ListType;
import com.booboot.vndbandroid.model.vndbandroid.Theme;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.booboot.vndbandroid.util.image.BlurIfDemoTransform;
import com.booboot.vndbandroid.util.image.Pixels;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean mainActivityExists = false;
    public static boolean shouldRefresh = false;
    private SearchView searchView;
    private List<VNTypeFragment> activeFragments = new ArrayList<>();
    private Fragment directSubfragment;
    public static int selectedItem;
    private NavigationView navigationView;
    private ConnectionReceiver connectionReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cache.loadFromCache(this);
        setTheme(Theme.THEMES.get(SettingsManager.getTheme(this)).getNoActionBarStyle());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // selected
                new int[]{-android.R.attr.state_checked}, // not selected
        };

        int[] colors = new int[]{
                Utils.getThemeColor(this, R.attr.colorAccent),
                Color.BLACK
        };
        navigationView.setItemTextColor(new ColorStateList(states, colors));
        navigationView.setItemIconTintList(new ColorStateList(states, colors));

        navigationView.getMenu().findItem(R.id.accountTitle).setTitle(SettingsManager.getUsername(this));
        View header = navigationView.getHeaderView(0);

        ImageView headerBackground = (ImageView) header.findViewById(R.id.headerBackground);
        Picasso.with(this).load(Theme.THEMES.get(SettingsManager.getTheme(this)).getWallpaper()).transform(new BlurIfDemoTransform(this)).into(headerBackground);

        FloatingActionButton floatingSearchButton = (FloatingActionButton) findViewById(R.id.floatingSearchButton);
        if (floatingSearchButton != null) {
            floatingSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, VNSearchActivity.class));
                }
            });
        }

        if (selectedItem > 0) {
            /* When the screen rotates, MainActivity is recreated so we have to go to where we were before (correct menu item and page) ! */
            int currentPage = VNListFragment.currentPage;
            goToFragment(selectedItem);
            VNListFragment.currentPage = currentPage;
        } else {
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }

        updateMenuCounters();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectionReceiver = new ConnectionReceiver();
            registerReceiver(connectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        mainActivityExists = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            /* Going back to home screen (don't use super.onBackPressed() because it would redirect to the LoginActivity underneath) */
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        /* Filter */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_filter).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Filter your VN list...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                for (VNTypeFragment activeFragment : activeFragments) {
                    activeFragment.filter(search);
                }
                return true;
            }
        });

        int searchIconId = searchView.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);
        searchIcon.setImageResource(R.drawable.ic_filter_list_white_24dp);

        return true;
    }

    public void onCreateSortMenu(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout linearLayout = new LinearLayout(this);
        final CheckBox reverseCheckbox = new CheckBox(this);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        reverseCheckbox.setLayoutParams(layoutParams);
        reverseCheckbox.setPadding(Pixels.px(20, this), reverseCheckbox.getPaddingTop(), reverseCheckbox.getPaddingRight(), reverseCheckbox.getPaddingBottom());
        reverseCheckbox.setText("Reverse order");
        reverseCheckbox.setChecked(SettingsManager.getReverseSort(this));

        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setPadding(Pixels.px(20, this), reverseCheckbox.getPaddingTop(), reverseCheckbox.getPaddingRight(), reverseCheckbox.getPaddingBottom());
        linearLayout.addView(reverseCheckbox);

        builder.setTitle("Sort VN list by :")
                .setSingleChoiceItems(Cache.SORT_OPTIONS, SettingsManager.getSort(this), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        SettingsManager.setReverseSort(MainActivity.this, reverseCheckbox.isChecked());
                        SettingsManager.setSort(MainActivity.this, which);
                        Cache.sortAll(MainActivity.this);
                        goToFragment(MainActivity.selectedItem);
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getListView().addFooterView(linearLayout);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, VNSearchActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        VNListFragment.currentPage = 0;
        return goToFragment(id);
    }

    public boolean goToFragment(int id) {
        Bundle args = new Bundle();
        selectedItem = id;

        if (id == R.id.nav_vnlist) {
            directSubfragment = new VNListFragment();
            args.putInt(VNListFragment.LIST_TYPE_ARG, ListType.VNLIST);
        } else if (id == R.id.nav_votelist) {
            directSubfragment = new VNListFragment();
            args.putInt(VNListFragment.LIST_TYPE_ARG, ListType.VOTELIST);
        } else if (id == R.id.nav_wishlist) {
            directSubfragment = new VNListFragment();
            args.putInt(VNListFragment.LIST_TYPE_ARG, ListType.WISHLIST);
        } else if (id == R.id.nav_stats) {
            directSubfragment = new DatabaseStatisticsFragment();
        } else if (id == R.id.nav_top) {
            directSubfragment = new RankingTopFragment();
        } else if (id == R.id.nav_popular) {
            directSubfragment = new RankingPopularFragment();
        } else if (id == R.id.nav_most_voted) {
            directSubfragment = new RankingMostVotedFragment();
        } else if (id == R.id.nav_newly_released) {
            directSubfragment = new RankingNewlyReleasedFragment();
        } else if (id == R.id.nav_newly_added) {
            directSubfragment = new RankingNewlyAddedFragment();
        } else if (id == R.id.nav_recommendations) {
            directSubfragment = new RecommendationsFragment();
        } else if (id == R.id.nav_settings) {
            directSubfragment = new PreferencesFragment();
        } else if (id == R.id.nav_about) {
            directSubfragment = new AboutFragment();
        } else if (id == R.id.nav_logout) {
            return logout();
        }

        directSubfragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, directSubfragment, "FRAGMENT").addToBackStack(null).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean logout() {
        VNDBServer.closeAll();
        Cache.clearCache(this);
        SettingsManager.setUserId(this, -1);

        RankingMostVotedFragment.options = null;
        RankingNewlyAddedFragment.options = null;
        RankingNewlyReleasedFragment.options = null;
        RankingPopularFragment.options = null;
        RankingTopFragment.options = null;
        RecommendationsFragment.recommendations = null;
        LoginActivity.autologin = false;

        startActivity(new Intent(this, LoginActivity.class));
        selectedItem = 0;
        finish();
        return true;
    }

    public void addActiveFragment(VNTypeFragment fragment) {
        this.activeFragments.add(fragment);
    }

    public void removeActiveFragment(VNTypeFragment fragment) {
        this.activeFragments.remove(fragment);
    }

    public void refreshVnlistFragment() {
        VNListFragment fragment = directSubfragment instanceof VNListFragment ? (VNListFragment) directSubfragment : null;
        Cache.sortAll(this);
        if (fragment != null) {
            fragment.refresh();
        }
        updateMenuCounters();
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void updateMenuCounters() {
        setMenuCounter(R.id.nav_vnlist, Cache.vnlist.size());
        setMenuCounter(R.id.nav_wishlist, Cache.wishlist.size());
        setMenuCounter(R.id.nav_votelist, Cache.votelist.size());
    }

    private void setMenuCounter(int itemId, int count) {
        if (navigationView != null) {
            TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
            view.setText(count > 0 ? String.valueOf(count) : null);
        }
    }

    private void addInfoToCrashlytics() {
        Crashlytics.setInt("VNS SIZE", Cache.vns.size());
        Crashlytics.setInt("VNLIST SIZE", Cache.vnlist.size());
        Crashlytics.setInt("VOTELIST SIZE", Cache.votelist.size());
        Crashlytics.setInt("WISHLIST SIZE", Cache.wishlist.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        VNDetailsActivity.goBackToVnlist = false;
        addInfoToCrashlytics();
        if (shouldRefresh) refreshVnlistFragment();
        shouldRefresh = false;
    }

    @Override
    protected void onDestroy() {
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
        mainActivityExists = false;
        super.onDestroy();
    }
}