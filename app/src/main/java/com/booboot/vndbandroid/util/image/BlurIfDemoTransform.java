package com.booboot.vndbandroid.util.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class BlurIfDemoTransform implements Transformation {
    private final static boolean DEMO = false;
    private Context context;

    public BlurIfDemoTransform(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        if (!DEMO) return bitmap;
        Bitmap res = BitmapTransformation.darkBlur(context, bitmap);
        bitmap.recycle();
        return res;
    }

    @Override
    public String key() {
        return "darkblur";
    }
}