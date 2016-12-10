package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndbandroid.Theme;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VNDBURLActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            Pattern pattern = Pattern.compile("\\.org/v([0-9]+)");
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                int vnId = Integer.parseInt(matcher.group(1));
                Cache.openVNDetails(this, vnId, new Callback() {
                    @Override
                    protected void config() {
                        finish();
                    }
                }, new Callback() {
                    @Override
                    protected void config() {
                        Callback.showToast(VNDBURLActivity.this, "You must be connected and register your VNDB.org account to open external links in the app.");
                        finish();
                    }
                });
            } else finish();
        } else finish();
    }
}
