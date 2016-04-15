package com.booboot.vndbandroid.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.booboot.vndbandroid.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by od on 26/03/2016.
 */
public class Lightbox {
    public static void set(final Context context, ImageView image, final String url) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.act_lightbox);
                dialog.setCancelable(true);

                final ImageView lightbox = (ImageView) dialog.findViewById(R.id.lightboxView);
                ImageLoader.getInstance().displayImage(url, lightbox);

                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                lightbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
