package com.booboot.vndbandroid.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.booboot.vndbandroid.api.bean.DbStats;
import com.booboot.vndbandroid.api.bean.Results;

/**
 * Created by od on 12/03/2016.
 */
public abstract class Callback {
    public String message;
    public Results results;
    public DbStats dbstats;

    protected abstract void config();

    public void call() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                config();
            }
        });
    }

    public static Callback errorCallback(final Context context) {
        return new Callback() {
            @Override
            public void config() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        };
    }
}
