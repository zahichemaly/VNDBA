package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VNDBFragment extends Fragment {
    protected int layout;
    protected View rootView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // [DEV] Causes crashes everywhere in async callbacks when views are set to null. Too much work to check everything so don't unbind in this project (like before).
        // Works best with RxJava.
 //       unbinder.unbind();
    }
}
