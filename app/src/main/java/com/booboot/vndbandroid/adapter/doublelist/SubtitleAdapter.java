package com.booboot.vndbandroid.adapter.doublelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.booboot.vndbandroid.R;

import java.util.List;

/**
 * Created by od on 06/04/2016.
 */
public class SubtitleAdapter extends BaseAdapter {
    private Context context;
    private List<DoubleListElement> elements;

    public SubtitleAdapter(Context context, List<DoubleListElement> elements) {
        this.context = context;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
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
            final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.simple_item_subtitle, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
        ImageButton itemButton = (ImageButton) convertView.findViewById(R.id.itemButton);

        DoubleListElement element = elements.get(position);

        title.setText(element.getLeftText());
        if (element.getRightText() != null && !element.getRightText().isEmpty()) {
            subtitle.setText(element.getRightText());
        } else {
            subtitle.setVisibility(View.GONE);
        }

        return convertView;
    }
}