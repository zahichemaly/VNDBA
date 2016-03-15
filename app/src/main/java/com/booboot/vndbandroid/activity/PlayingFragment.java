package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.json.JSON;
import com.booboot.vndbandroid.util.Callback;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 09/03/2016.
 */
public class PlayingFragment extends Fragment {
    private MaterialListView materialListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playing_fragment, container, false);

        Log.d("D", "Creating a new Playing Fragment...");
        materialListView = (MaterialListView) rootView.findViewById(R.id.materialListView);

        VNDBServer.get("vnlist", "basic", "(uid = 0)", null, getActivity(), new Callback() {
            @Override
            public void config() {
                List<Integer> ids = new ArrayList<>();
                for (Item vnlistItem : results.getItems()) {
                    ids.add(vnlistItem.getVn());
                }
                try {
                    VNDBServer.get("vn", "basic,details", "(id = " + JSON.mapper.writeValueAsString(ids) + ")", null, getActivity(), new Callback() {
                        @Override
                        protected void config() {
                            for (final Item vn : results.getItems()) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        final Drawable image = drawableFromUrl(vn.getImage());
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                CardProvider cardProvider = new Card.Builder(getActivity())
                                                        .withProvider(new CardProvider())
                                                        .setLayout(R.layout.vn_card_layout)
                                                        .setTitle(vn.getTitle())
                                                        .setSubtitle(vn.getOriginal())
                                                        .setSubtitleColor(Color.BLACK)
                                                        .setTitleGravity(Gravity.END)
                                                        .setSubtitleGravity(Gravity.END)
                                                        .setDescription(vn.getReleased())
                                                        .setDescriptionGravity(Gravity.END);

                                                cardProvider = cardProvider.setDrawable(image);
                                                // .setDrawable(R.drawable.sample_0)
                                                Card card = cardProvider.setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                                                    @Override
                                                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                                                        requestCreator.fit();
                                                    }
                                                }).endConfig().build();

                                                materialListView.getAdapter().add(card);
                                                materialListView.scrollToPosition(0);
                                            }
                                        });
                                    }
                                }.start();
                            }
                        }
                    }, new Callback() {
                        @Override
                        protected void config() {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }, new Callback() {
            @Override
            public void config() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    public Drawable drawableFromUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap x = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(getActivity().getResources(), x);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
