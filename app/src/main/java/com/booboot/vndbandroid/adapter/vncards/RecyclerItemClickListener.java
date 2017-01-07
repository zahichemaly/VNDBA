package com.booboot.vndbandroid.adapter.vncards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        void onItemClick(final CardView cardView, int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (true) return;
                CardView childView = (CardView) mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && mListener != null) {
                    // mListener.onItemLongClick(childView, mRecyclerView.getChildPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        if (view == null) return false;
        CardView childView = (CardView) view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}