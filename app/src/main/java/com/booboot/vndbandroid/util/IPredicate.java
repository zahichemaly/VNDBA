package com.booboot.vndbandroid.util;

/**
 * Created by od on 16/04/2016.
 */
public interface IPredicate<T> {
    boolean apply(T element);
}
