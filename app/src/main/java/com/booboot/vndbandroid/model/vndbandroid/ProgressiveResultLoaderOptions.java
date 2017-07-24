package com.booboot.vndbandroid.model.vndbandroid;

import com.booboot.vndbandroid.adapter.vncards.Card;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;
import com.booboot.vndbandroid.model.vndb.VN;

import java.util.List;

public class ProgressiveResultLoaderOptions {
    private List<Card> cards;
    private int currentPage;
    private boolean moreResults;

    public static ProgressiveResultLoaderOptions build(ProgressiveResultLoader progressiveResultLoader) {
        if (progressiveResultLoader == null) return null;
        ProgressiveResultLoaderOptions options = new ProgressiveResultLoaderOptions();
        options.cards = progressiveResultLoader.getCards();
        if (options.cards.isEmpty()) return null;
        options.currentPage = progressiveResultLoader.getCurrentPage();
        options.moreResults = progressiveResultLoader.isMoreResults();
        return options;
    }

    public boolean isComplete() {
        if (cards == null || cards.isEmpty()) return false;
        for (Card card : cards) {
            if (card == null) continue;
            int vnId = card.getVnId();
            VN vn = Cache.vns.get(vnId);
            if (vn == null) return false;
        }
        return true;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    public void setMoreResults(boolean moreResults) {
        this.moreResults = moreResults;
    }
}
