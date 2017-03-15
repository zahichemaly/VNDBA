package com.booboot.vndbandroid.model.vndbandroid;

import com.booboot.vndbandroid.adapter.vncards.Card;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;

import java.util.List;

/**
 * Created by od on 15/03/2017.
 */

public class ProgressiveResultLoaderOptions {
    private List<Card> cards;
    private int currentPage;
    private boolean moreResults;

    public static ProgressiveResultLoaderOptions build(ProgressiveResultLoader progressiveResultLoader) {
        if (progressiveResultLoader == null) return null;
        ProgressiveResultLoaderOptions options = new ProgressiveResultLoaderOptions();
        options.cards = progressiveResultLoader.getCards();
        options.currentPage = progressiveResultLoader.getCurrentPage();
        options.moreResults = progressiveResultLoader.isMoreResults();
        return options;
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
