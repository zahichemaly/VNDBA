package com.booboot.vndbandroid.util;

import android.app.Application;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.ErrorActivity;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import butterknife.ButterKnife;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import io.fabric.sdk.android.Fabric;

/**
 * Created by od on 22/05/2016.
 */
public class VNDBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.vndb_logo_80);
        CustomActivityOnCrash.setErrorActivityClass(ErrorActivity.class);
        CustomActivityOnCrash.install(this);

        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());

        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}