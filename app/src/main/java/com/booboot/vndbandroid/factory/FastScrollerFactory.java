package com.booboot.vndbandroid.factory;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.util.Pixels;
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

/**
 * Created by od on 18/05/2016.
 */
public class FastScrollerFactory {
    public static void get(Activity activity, View rootView, MaterialListView materialListView, final SwipeRefreshLayout refreshLayout) {
        RecyclerFastScroller fastScroller = (RecyclerFastScroller) (rootView == null ? activity.findViewById(R.id.fastScroller) : rootView.findViewById(R.id.fastScroller));
        fastScroller.attachRecyclerView(materialListView);
        fastScroller.setTouchTargetWidth(Pixels.px(32, activity));

        fastScroller.setOnHandleTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                refreshLayout.setEnabled(false);
            } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                refreshLayout.setEnabled(true);
            }
            return false;
        });
    }
}