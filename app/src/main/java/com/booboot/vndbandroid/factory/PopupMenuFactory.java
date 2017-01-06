package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.image.Pixels;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 28/11/2016.
 */
public class PopupMenuFactory {
    public interface Callback {
        void create(View content);
    }

    public static PopupWindow get(Activity context, int layout, final View anchor, PopupWindow oldPopup, Callback callback) {
        if (oldPopup != null && oldPopup.isTouchable()) {
            oldPopup.setTouchable(false);
            return null;
        }

        final PopupWindow popupWindow = new PopupWindow(context);
        int width = Pixels.px(250, context);
        popupWindow.setWidth(width);
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

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
                    boolean insideAnchor = inside(event.getRawX(), event.getRawY(), anchor);
                    if (!insideAnchor) popupWindow.setTouchable(false);
                    return true;
                }
                return false;
            }
        });

        int[] mCoordBuffer = new int[2];
        anchor.getLocationOnScreen(mCoordBuffer);
        int xoff = width - (Utils.screenWidth(context) - mCoordBuffer[0]);
        xoff = -xoff;
        popupWindow.showAsDropDown(anchor, xoff < 0 ? xoff : 0, 0);

        return popupWindow;
    }

    private static boolean inside(float x, float y, View v) {
        int[] mCoordBuffer = new int[2];
        v.getLocationOnScreen(mCoordBuffer);
        return mCoordBuffer[0] + v.getWidth() > x &&    // right edge
                mCoordBuffer[1] + v.getHeight() > y &&   // bottom edge
                mCoordBuffer[0] < x &&                   // left edge
                mCoordBuffer[1] < y;                     // top edge
    }
}
