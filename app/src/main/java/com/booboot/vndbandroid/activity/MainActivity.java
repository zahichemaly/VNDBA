package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.ListType;
import com.booboot.vndbandroid.settings.SettingsManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
}
