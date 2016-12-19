package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Pixels;

/**
 * Created by od on 28/11/2016.
 */
public class PopupMenuFactory {
    public interface Callback {
        void create(View content);
    }

    public static PopupWindow get(Activity context, int layout, final View anchor, PopupWindow oldPopup, Callback callback) {
        if (oldPopup != null && !oldPopup.isTouchable()) {
            return null;
        }

        final PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setWidth(Pixels.px(250, context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(Pixels.px(10, context));
        }
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = layoutInflater.inflate(layout, null);

        callback.create(content);

        popupWindow.setContentView(content);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.transparent_rounded_white_background));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    popupWindow.setTouchable(false);
                    return true;
                }
                return false;
            }
        });
        popupWindow.showAsDropDown(anchor);

        return popupWindow;
    }
}
