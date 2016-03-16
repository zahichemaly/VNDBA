package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.Item;

public class VNDetailsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Item vn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(PlayingFragment.VN_ARG);

        actionBar = getSupportActionBar();
        actionBar.setTitle(vn.getTitle());
        Log.d("D", vn.getImage());
        actionBar.setDisplayHomeAsUpEnabled(true);
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
