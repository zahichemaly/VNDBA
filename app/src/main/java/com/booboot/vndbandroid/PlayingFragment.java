package com.booboot.vndbandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.RequestCreator;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by od on 09/03/2016.
 */
public class PlayingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playing_fragment, container, false);

        MaterialListView mListView = (MaterialListView) rootView.findViewById(R.id.material_listview);
        Card card = new Card.Builder(getActivity())
                .withProvider(new CardProvider())
                .setLayout(R.layout.vn_card_layout)
                .setTitle("Card number 1")
                .setTitleGravity(Gravity.END)
                .setDescription("Lorem ipsum dolor sit amet")
                .setDescriptionGravity(Gravity.END)
                .setDrawable(R.drawable.sample_0)
                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                        requestCreator.fit();
                    }
                })
                .addAction(R.id.left_text_button, new TextViewAction(getActivity())
                        .setText("Izquierda")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(getActivity(), "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                card.getProvider().setTitle("CHANGED ON RUNTIME");
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(getActivity())
                        .setText("Derecha")
                        .setTextResourceColor(R.color.orange_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(getActivity(), "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                card.dismiss();
                            }
                        }))
                .endConfig()
                .build();

        mListView.getAdapter().add(card);
        mListView.getAdapter().add(card);
        mListView.getAdapter().add(card);
        mListView.getAdapter().add(card);
        mListView.getAdapter().add(card);
        mListView.scrollToPosition(0);

        return rootView;
    }
}
