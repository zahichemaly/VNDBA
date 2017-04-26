package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

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
            if (matcher.find()) { // we found a VN in the link
                /* If the cache is not loaded, load it so we can show the status, wish and vote of the user for this VN
                (and it may also avoid "get vn"/"get character"/"get release" calls if they're already in the DB) */
                if (!Cache.loadedFromCache) {
                    if (!SettingsManager.isEmptyAccount(this) && !Cache.loadFromCache(this)) {
                        /* The DB is empty and it's not an empty account (i.e. newly-created account) : we now are 100% sure that the user has not been connected to the app,
                        so we leave (because we don't support opening deep links without being connected) */
                        Callback.showToast(this, "You must register your VNDB.org account in the app to open VNDB.org links.");
                        finish();
                        return;
                    }
                    /* The cache has been loaded successfully or the account is empty : it's ok, we can open the deep link without corrupting anything */
                }

                int vnId = Integer.parseInt(matcher.group(1));
                Cache.openVNDetails(this, vnId, new Callback() {
                    @Override
                    protected void config() {
                        finish();
                    }
                }, new Callback() {
                    @Override
                    protected void config() {
                        Callback.showToast(VNDBURLActivity.this, message);
                        finish();
                    }
                });
            } else finish();
        } else finish();
    }
}
