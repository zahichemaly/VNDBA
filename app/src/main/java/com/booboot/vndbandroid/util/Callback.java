package com.booboot.vndbandroid.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.booboot.vndbandroid.model.vndb.DbStats;
import com.booboot.vndbandroid.model.vndb.Results;
import com.booboot.vndbandroid.model.vnstat.VNStatItem;

/**
 * Created by od on 12/03/2016.
 */
public abstract class Callback {
    public String message;
    public Results results;
    public DbStats dbstats;
    public VNStatItem vnStatResults;
    private static Toast toast;
    private static CountDownTimer toastTimer;

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
                showToast(context, message);
            }
        };
    }

    public static void showToast(final Context context, final String message) {
        if (message == null || context == null) return;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (toast != null) toast.cancel();
                if (toastTimer != null) toastTimer.cancel();

                toast = Toast.makeText(context, message, message.length() > 40 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.show();
                if (message.length() > 90) {
                    toastTimer = new CountDownTimer(8000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.cancel();
                        }
                    }.start();
                }
            }
        });
    }
}
