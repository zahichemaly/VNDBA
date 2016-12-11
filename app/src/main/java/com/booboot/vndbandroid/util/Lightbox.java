package com.booboot.vndbandroid.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by od on 26/03/2016.
 */
public class Lightbox implements Target {
    private Context context;
    private ImageView lightbox;
    private static Dialog dialog;

    private Lightbox(Context context, ImageView lightbox, Dialog dialog) {
        this.context = context;
        this.lightbox = lightbox;
        Lightbox.dialog = dialog;
    }

    public static void set(final Context context, ImageView image, final String url) {
        if (url == null) return;

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.act_lightbox);
                dialog.setCancelable(true);

                final ImageView lightbox = (ImageView) dialog.findViewById(R.id.lightboxView);
                Picasso.with(context).load(url).into(new Lightbox(context, lightbox, dialog));
            }
        });
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        lightbox.setMinimumWidth(Pixels.px(bitmap.getWidth(), context));
        lightbox.setMinimumHeight(Pixels.px(bitmap.getHeight(), context));
        lightbox.setImageBitmap(bitmap);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        lightbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        dialog.dismiss();
        Callback.showToast(context, "Could not load image.");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
