package com.booboot.vndbandroid.adapter.vncards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by od on 22/11/2016.
 */
public class VNCardsListView extends RecyclerView {
    public VNCardsListView(Context context) {
        super(context);
    }

    public VNCardsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VNCardsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public VNCardsAdapter getAdapter() {
        return (VNCardsAdapter) super.getAdapter();
    }
}
