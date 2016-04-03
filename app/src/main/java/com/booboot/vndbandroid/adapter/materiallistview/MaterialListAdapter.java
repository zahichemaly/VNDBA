package com.booboot.vndbandroid.adapter.materiallistview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardLayout;
import com.dexafree.materialList.card.event.DismissEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.ViewHolder> implements Observer, Filterable {
    private final MaterialListView.OnSwipeAnimation mSwipeAnimation;
    private final MaterialListView.OnAdapterItemsChanged mItemAnimation;
    private final List<Card> cardList = new ArrayList<>();
    private List<Card> filteredCardList = cardList;
    private ItemFilter mFilter = new ItemFilter();

    public MaterialListAdapter(@NonNull final MaterialListView.OnSwipeAnimation swipeAnimation,
                               @NonNull final MaterialListView.OnAdapterItemsChanged itemAnimation) {
        mSwipeAnimation = swipeAnimation;
        mItemAnimation = itemAnimation;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardLayout view;

        public ViewHolder(@NonNull final View v) {
            super(v);
            view = (CardLayout) v;
        }

        public void build(Card card) {
            view.build(card);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.build(getCard(position));
    }

    @Override
    public int getItemCount() {
        return filteredCardList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return filteredCardList.get(position).getProvider().getLayout();
    }

    /**
     * Add a Card at a specific position with or without a scroll animation.
     *
     * @param position
     *         of the card to insert.
     * @param card
     *         to insert.
     * @param scroll
     *         will trigger an animation if it is set to <code>true</code> otherwise not.
     */
    public void add(final int position, @NonNull final Card card, final boolean scroll) {
        cardList.add(position, card);
        card.getProvider().addObserver(this);
        mItemAnimation.onAddItem(position, scroll);
        notifyItemInserted(position); // Triggers the animation!
    }

    /**
     * Add a Card at a specific position.
     *
     * @param position
     *         of the card to insert.
     * @param card
     *         to insert.
     */
    public void add(final int position, @NonNull final Card card) {
        add(position, card, true);
    }

    /**
     * Add a Card at the start.
     *
     * @param card
     *         to add at the start.
     */
    public void addAtStart(@NonNull final Card card) {
        add(0, card);
    }

    /**
     * Add a Card.
     *
     * @param card
     *         to add.
     */
    public void add(@NonNull final Card card) {
        add(cardList.size(), card);
    }

    /**
     * Add all Cards.
     *
     * @param cards
     *         to add.
     */
    public void addAll(@NonNull final Card... cards) {
        addAll(Arrays.asList(cards));
    }

    /**
     * Add all Cards.
     *
     * @param cards
     *         to add.
     */
    public void addAll(@NonNull final Collection<Card> cards) {
        int index = 0;
        for (Card card : cards) {
            add(index++, card, false);
        }
    }

    /**
     * Remove a Card withProvider or without an animation.
     *
     * @param card
     *         to remove.
     * @param animate
     *         {@code true} to animate the remove process or {@code false} otherwise.
     */
    public void remove(@NonNull final Card card, boolean animate) {
        if (card.isDismissible()) {
            card.getProvider().deleteObserver(this);
            if (animate) {
                mSwipeAnimation.animate(getPosition(card));
            } else {
                cardList.remove(card);
                mItemAnimation.onRemoveItem();
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Clears the list from all Cards (even if they are not dismissable).
     */
    public void clearAll() {
        while (!cardList.isEmpty()) {
            final Card card = cardList.get(0);
            card.setDismissible(true);
            remove(card, false);
            notifyItemRemoved(0);
        }
    }

    /**
     * Clears the list from all Cards (only if dismissable).
     */
    public void clear() {
        for (int index = 0; index < cardList.size(); ) {
            final Card card = cardList.get(index);
            if (!card.isDismissible()) {
                index++;
            }
            remove(card, false);
            notifyItemRemoved(index);
        }
    }

    /**
     * Is the list empty?
     *
     * @return {@code true} if the list is empty or {@code false} otherwise.
     */
    public boolean isEmpty() {
        return filteredCardList.isEmpty();
    }

    /**
     * Get a Card at the specified position.
     *
     * @param position
     *         of the Card.
     * @return the Card or {@code null} if the position is outside of the list range.
     */
    @Nullable
    public Card getCard(int position) {
        if (position >= 0 && position < filteredCardList.size()) {
            return filteredCardList.get(position);
        }
        return null;
    }

    /**
     * Get the position of a specified Card.
     *
     * @param card
     *         to get the position of.
     * @return the position.
     */
    public int getPosition(@NonNull Card card) {
        return filteredCardList.indexOf(card);
    }

    @Override
    public void update(final Observable observable, final Object data) {
        if (data instanceof DismissEvent) {
            remove(((DismissEvent) data).getCard(), true);
        }
        if (data instanceof Card) {
            notifyDataSetChanged();
        }
    }

    public class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().trim().toLowerCase();
            FilterResults results = new FilterResults();

            int count = cardList.size();
            final ArrayList<Card> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                String filterableString = cardList.get(i).getProvider().getTitle();
                if (filterableString.trim().toLowerCase().contains(filterString)) {
                    nlist.add(cardList.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredCardList = (ArrayList<Card>) results.values;
            notifyDataSetChanged();
        }
    }
}
