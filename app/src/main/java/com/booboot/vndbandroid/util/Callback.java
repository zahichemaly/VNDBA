package com.booboot.vndbandroid.util;

import android.os.Handler;
import android.os.Looper;

import com.booboot.vndbandroid.api.bean.Results;

/**
 * Created by od on 12/03/2016.
 */
public abstract class Callback {
    public String message;
    public Results results;

    protected abstract void config();

    public void call() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                config();
            }
        });
    }
}
