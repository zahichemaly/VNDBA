package com.booboot.vndbandroid.util;

import android.app.Application;

import com.booboot.vndbandroid.R;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by od on 22/05/2016.
 */
public class VNDBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.vndb_logo_80);
        CustomActivityOnCrash.install(this);
    }
}