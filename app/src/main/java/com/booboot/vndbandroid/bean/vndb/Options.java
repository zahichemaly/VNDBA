package com.booboot.vndbandroid.bean.vndb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Options extends VNDBCommand {
    private int page = 1;
    private int results = 25;
    private String sort;
    private boolean reverse;
    private boolean fetchAllPages;
    private int numberOfPages;

    public static Options create(boolean fetchAllPages, int numberOfPages) {
        Options options = new Options();
        options.fetchAllPages = fetchAllPages;
        options.numberOfPages = numberOfPages;
        return options;
    }

    public static Options create(int page, int results, String sort, boolean reverse, boolean fetchAllPages, int numberOfPages) {
        Options options = new Options();
        options.page = page;
        options.results = results;
        options.sort = sort;
        options.reverse = reverse;
        options.fetchAllPages = fetchAllPages;
        options.numberOfPages = numberOfPages;
        return options;
    }

    public static Options create(Options other) {
        Options options = new Options();
        options.page = other.page;
        options.results = other.results;
        options.sort = other.sort;
        options.reverse = other.reverse;
        options.fetchAllPages = other.fetchAllPages;
        options.numberOfPages = other.numberOfPages;
        return options;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public void setFetchAllPages(boolean fetchAllPages) {
        this.fetchAllPages = fetchAllPages;
    }

    public boolean isFetchAllPages() {
        return fetchAllPages;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
