package com.booboot.vndbandroid.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.booboot.vndbandroid.bean.vndb.DbStats;
import com.booboot.vndbandroid.bean.vndb.Results;
import com.booboot.vndbandroid.bean.vnstat.VNStatItem;
import com.booboot.vndbandroid.bean.vnstat.VNStatResults;

import java.util.concurrent.CountDownLatch;

/**
 * Created by od on 12/03/2016.
 */
public abstract class Callback {
    public String message;
    public Results results;
    public DbStats dbstats;
    public VNStatItem vnStatResults;
    public static CountDownLatch countDownLatch;

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
                if (countDownLatch != null) countDownLatch.countDown();
            }
        };
    }

    public static void showToast(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.show();
                if (message.length() > 90) {
                    new CountDownTimer(8000, 1000) {
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
