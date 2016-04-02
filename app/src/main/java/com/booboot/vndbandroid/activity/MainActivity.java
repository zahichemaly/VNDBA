package com.booboot.vndbandroid.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.ListType;
import com.booboot.vndbandroid.settings.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchView searchView;
    private List<VNTypeFragment> activeFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView headerUsername = (TextView) header.findViewById(R.id.headerUsername);
        headerUsername.setText(SettingsManager.getUsername(this));
        LinearLayout headerBackground = (LinearLayout) header.findViewById(R.id.headerBackground);
        headerBackground.setBackground(getResources().getDrawable(R.drawable.bg_5, getTheme()));

        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        int permsRequestCode = 200;
        requestPermissions(perms, permsRequestCode);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

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
                    activeFragment.getMaterialListView().getAdapter().getFilter().filter(search.toString());
                }
                return true;
            }
        });

        int searchIconId = searchView.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);
        searchIcon.setImageResource(R.drawable.ic_action_filter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_filter:
                Log.d("D", "ACTION FILTER");
                return true;

            case R.id.action_search:
                Log.d("D", "ACTION SEARCH");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        return goToFragment(id);
    }

    private boolean goToFragment(int id) {
        VNListFragment nextFrag = new VNListFragment();
        Bundle args = new Bundle();

        if (id == R.id.nav_vnlist) {
            args.putInt(VNListFragment.LIST_TYPE_ARG, ListType.VNLIST);
        } else if (id == R.id.nav_votelist) {
            args.putInt(VNListFragment.LIST_TYPE_ARG, ListType.VOTELIST);
        } else if (id == R.id.nav_wishlist) {
            args.putInt(VNListFragment.LIST_TYPE_ARG, ListType.WISHLIST);
        } else if (id == R.id.nav_logout) {
            args.putInt("ARG", 5);
        }

        nextFrag.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, nextFrag, "FRAGMENT").addToBackStack(null).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addActiveFragment(VNTypeFragment fragment) {
        this.activeFragments.add(fragment);
    }

    public void removeActiveFragment(VNTypeFragment fragment) {
        this.activeFragments.remove(fragment);
    }

    public SearchView getSearchView() {
        return searchView;
    }
}
