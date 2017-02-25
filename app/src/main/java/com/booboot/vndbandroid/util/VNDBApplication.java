package com.booboot.vndbandroid.util;

import android.app.Application;
import android.content.Context;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.ErrorActivity;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import io.fabric.sdk.android.Fabric;

/**
 * Created by od on 22/05/2016.
 */
public class VNDBApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().clearDiskCache();

        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.vndb_logo_80);
        CustomActivityOnCrash.setErrorActivityClass(ErrorActivity.class);
        CustomActivityOnCrash.install(this);

        Fabric.with(this, new Crashlytics());

        applicationContext = getApplicationContext();
    }
}