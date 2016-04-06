package com.booboot.vndbandroid.adapter.dbstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;

/**
 * Created by od on 06/04/2016.
 */
public class DatabaseStatisticsAdapter extends BaseAdapter {
    private Context context;
    private String[] leftText;
    private String[] rightText;

    public DatabaseStatisticsAdapter(Context context, String[] leftText, String[] rightText) {
        this.context = context;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    @Override
    public int getCount() {
        return leftText.length;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_text, null);
        }

        ImageView itemLeftImage = (ImageView) convertView.findViewById(R.id.itemLeftImage);
        TextView itemLeftText = (TextView) convertView.findViewById(R.id.itemLeftText);
        TextView itemRightText = (TextView) convertView.findViewById(R.id.itemRightText);
        ImageView itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);

        itemLeftImage.setVisibility(View.GONE);
        itemRightImage.setVisibility(View.GONE);

        itemLeftText.setText(leftText[position]);
        itemRightText.setText(rightText[position]);

        return convertView;
    }
}