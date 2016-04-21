package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Options extends VNDBCommand {
    private int page;
    private int results;
    private String sort;
    private boolean reverse;
    private boolean fetchAllPages;
    private boolean useCacheIfError;

    public static Options create(boolean fetchAllPages, boolean useCacheIfError) {
        Options options = new Options();
        options.page = 1;
        options.results = 25;
        options.sort = null;
        options.reverse = false;
        options.fetchAllPages = fetchAllPages;
        options.useCacheIfError = useCacheIfError;
        return options;
    }

    public static Options create(int page, int results, String sort, boolean reverse, boolean fetchAllPages, boolean useCacheIfError) {
        Options options = new Options();
        options.page = page;
        options.results = results;
        options.sort = sort;
        options.reverse = reverse;
        options.fetchAllPages = fetchAllPages;
        options.useCacheIfError = useCacheIfError;
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

    public boolean isUseCacheIfError() {
        return useCacheIfError;
    }

    public void setUseCacheIfError(boolean useCacheIfError) {
        this.useCacheIfError = useCacheIfError;
    }
}
