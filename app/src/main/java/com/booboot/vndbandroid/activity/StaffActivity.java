package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndb.Options;
import com.booboot.vndbandroid.model.vndbandroid.Theme;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.crashlytics.android.Crashlytics;

public class StaffActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public final static String STAFF_ID = "STAFF_ID";
    private Item staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cache.loadFromCache(this);

        int id = getIntent().getIntExtra(STAFF_ID, -1);
        Crashlytics.setInt("LAST STAFF VISITED", id);
        staff = Cache.staff.get(id);

        if (savedInstanceState != null) {
            if (staff == null) {
                if (id < 0) id = savedInstanceState.getInt(STAFF_ID);
                staff = Cache.staff.get(id);
            }
        }

        if (staff == null) { // #97
            VNDBServer.get("staff", Cache.STAFF_FLAGS, "(id = " + id + ")", Options.create(false, 1), 0, this, new Callback() {
                @Override
                protected void config() {
                    if (!results.getItems().isEmpty()) {
                        staff = results.getItems().get(0);
                        Cache.staff.put(staff.getId(), staff);
                        recreate(); // #97
                    }
                }
            }, new Callback() {
                @Override
                protected void config() {
                    Callback.showToast(StaffActivity.this, message);
                    finish();
                }
            });
        } else {
            init();
        }
    }

    private void init() {
        setTheme(Theme.THEMES.get(SettingsManager.getTheme(this)).getStyle());
        setContentView(R.layout.staff_activity);

    }

    @Override
    public void onRefresh() {
        // TODO: allow the user the refresh the info like VNDetailsActivity.onRefresh()
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STAFF_ID, staff != null ? staff.getId() : -1);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }
}