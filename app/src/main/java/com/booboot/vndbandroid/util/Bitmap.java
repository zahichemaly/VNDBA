package com.booboot.vndbandroid.util;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by od on 15/03/2016.
 */
public class Bitmap {
    public static Drawable drawableFromUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            android.graphics.Bitmap x = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(x);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
