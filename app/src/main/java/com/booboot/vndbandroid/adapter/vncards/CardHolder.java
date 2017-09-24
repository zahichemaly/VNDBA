package com.booboot.vndbandroid.adapter.vncards;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by od on 22/11/2016.
 */
public class CardHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cardView)
    protected CardView cardView;

    @BindView(R.id.image)
    protected ImageView image;

    @BindView(R.id.title)
    protected TextView title;

    @BindView(R.id.subtitle)
    protected TextView subtitle;

    @BindView(R.id.statusButton)
    protected TextView statusButton;

    @BindView(R.id.wishlistButton)
    protected TextView wishlistButton;

    @BindView(R.id.votesButton)
    protected TextView votesButton;

    public CardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}