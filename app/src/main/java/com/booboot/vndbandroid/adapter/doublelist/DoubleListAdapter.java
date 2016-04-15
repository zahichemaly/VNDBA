package com.booboot.vndbandroid.adapter.doublelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Pixels;

/**
 * Created by od on 06/04/2016.
 */
public class DoubleListAdapter extends BaseAdapter {
    private Context context;
    private DoubleListElement[] elements;

    public DoubleListAdapter(Context context, DoubleListElement[] elements) {
        this.context = context;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.length;
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
            convertView = layoutInflater.inflate(R.layout.list_item_text, null);
        }
        convertView.findViewById(R.id.itemLeftImage).setVisibility(View.GONE);
        convertView.findViewById(R.id.itemRightImage).setVisibility(View.GONE);
        TextView itemLeftText = (TextView) convertView.findViewById(R.id.itemLeftText);
        TextView itemRightText = (TextView) convertView.findViewById(R.id.itemRightText);

        DoubleListElement element = elements[position];

        if (element.isDisplayRightTextOnly()) {
            itemLeftText.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((LinearLayout) itemRightText.getParent()).getLayoutParams();
            params.setMarginStart(Pixels.px(15, context));
            itemRightText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        } else {
            itemLeftText.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((LinearLayout) itemRightText.getParent()).getLayoutParams();
            params.setMarginStart(Pixels.px(30, context));
            itemRightText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }

        itemLeftText.setPadding(Pixels.px(15, context), itemLeftText.getPaddingTop(), itemLeftText.getPaddingRight(), itemLeftText.getPaddingBottom());
        itemLeftText.setText(element.getLeftText());
        itemRightText.setText(element.getRightText());
        return convertView;
    }
}