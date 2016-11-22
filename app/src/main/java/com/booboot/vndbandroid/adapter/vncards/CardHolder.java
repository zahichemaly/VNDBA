package com.booboot.vndbandroid.adapter.vncards;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;

/**
 * Created by od on 22/11/2016.
 */
public class CardHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private ImageView image;
    private TextView title, subtitle;
    private Button statusButton, wishlistButton, votesButton;

    public CardHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        image = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        statusButton = (Button) itemView.findViewById(R.id.statusButton);
        wishlistButton = (Button) itemView.findViewById(R.id.wishlistButton);
        votesButton = (Button) itemView.findViewById(R.id.votesButton);
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

    public Button getStatusButton() {
        return statusButton;
    }

    public void setStatusButton(Button statusButton) {
        this.statusButton = statusButton;
    }

    public Button getWishlistButton() {
        return wishlistButton;
    }

    public void setWishlistButton(Button wishlistButton) {
        this.wishlistButton = wishlistButton;
    }

    public Button getVotesButton() {
        return votesButton;
    }

    public void setVotesButton(Button votesButton) {
        this.votesButton = votesButton;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }
}