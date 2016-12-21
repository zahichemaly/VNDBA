package com.booboot.vndbandroid.adapter.vncards;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 22/11/2016.
 */
public class CardHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private ImageView image;
    private TextView title, subtitle;
    private TextView statusButton;
    private TextView wishlistButton;
    private TextView votesButton;

    public CardHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        image = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        statusButton = (TextView) itemView.findViewById(R.id.statusButton);
        wishlistButton = (TextView) itemView.findViewById(R.id.wishlistButton);
        votesButton = (TextView) itemView.findViewById(R.id.votesButton);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(TextView subtitle) {
        this.subtitle = subtitle;
    }

    public TextView getStatusButton() {
        return statusButton;
    }

    public void setStatusButton(TextView statusButton) {
        this.statusButton = statusButton;
    }

    public TextView getWishlistButton() {
        return wishlistButton;
    }

    public void setWishlistButton(TextView wishlistButton) {
        this.wishlistButton = wishlistButton;
    }

    public TextView getVotesButton() {
        return votesButton;
    }

    public void setVotesButton(TextView votesButton) {
        this.votesButton = votesButton;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }
}