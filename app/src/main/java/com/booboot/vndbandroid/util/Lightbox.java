package com.booboot.vndbandroid.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by od on 26/03/2016.
 */
public class Lightbox implements ImageLoadingListener {
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

        image.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.act_lightbox);
            dialog.setCancelable(true);

            final ImageView lightbox = (ImageView) dialog.findViewById(R.id.lightboxView);
            ImageLoader.getInstance().loadImage(url, new Lightbox(context, lightbox, dialog));
        });
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        dialog.dismiss();
        ImageLoader.getInstance().cancelDisplayTask(lightbox);
        Toast.makeText(context, "Could not load image : " + failReason.getType(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        lightbox.setMinimumWidth(Pixels.px(loadedImage.getWidth(), context));
        lightbox.setMinimumHeight(Pixels.px(loadedImage.getHeight(), context));
        lightbox.setImageBitmap(loadedImage);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        lightbox.setOnClickListener(v1 -> {
            ImageLoader.getInstance().cancelDisplayTask(lightbox);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
    }
}
