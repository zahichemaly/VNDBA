package com.booboot.vndbandroid.adapter.vncards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Utils;
import com.booboot.vndbandroid.util.image.BlurIfDemoTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 22/11/2016.
 */
public class VNCardsAdapter extends RecyclerView.Adapter<CardHolder> implements Filterable {
    private Context context;
    private List<Card> cards;
    private List<Card> filteredCards;
    private ItemFilter mFilter = new ItemFilter();

    public VNCardsAdapter(Context context) {
        this.context = context;
        this.cards = new ArrayList<>();
        this.filteredCards = cards;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vn_card, parent, false);
        return new CardHolder(v);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Card card = getCard(position);

        if (card.getImageId() > 0)
            holder.getImage().setImageResource(card.getImageId());
        else
            Picasso.with(context).load(card.getImageUrl()).transform(new BlurIfDemoTransform(context)).into(holder.getImage());

        holder.getTitle().setText(card.getTitle());
        holder.getSubtitle().setText(card.getSubtitle());
        holder.getStatusButton().setText(card.getStatus());
        holder.getWishlistButton().setText(card.getWish());
        holder.getVotesButton().setText(card.getVote());
        holder.getCardView().setTag(card.getVnId());

        Utils.setElevation(context, holder.getStatusButton(), 8);
        Utils.setElevation(context, holder.getWishlistButton(), 8);
        Utils.setElevation(context, holder.getVotesButton(), 8);
    }

    @Override
    public int getItemCount() {
        return filteredCards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
        notifyDataSetChanged();
    }

    public Card getCard(int i) {
        if (i >= 0 && i < filteredCards.size())
            return filteredCards.get(i);
        return null;
    }

    public void clearAll() {
        cards.clear();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().trim().toLowerCase();
            FilterResults results = new FilterResults();

            int count = cards.size();
            final ArrayList<Card> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                String filterableString = cards.get(i).getTitle();
                if (filterableString.trim().toLowerCase().contains(filterString)) {
                    nlist.add(cards.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredCards = (ArrayList<Card>) results.values;
            notifyDataSetChanged();
        }
    }
}
