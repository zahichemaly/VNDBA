package com.booboot.vndbandroid.adapter;

/**
 * Created by od on 18/03/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Lightbox;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedHashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, VNDetailsElement> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle, LinkedHashMap<String, VNDetailsElement> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).getData().get(expandedListPosition);
    }

    public int getChildLayout(int listPosition) {
        int type = this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).getType();
        if (type == VNDetailsElement.TYPE_TEXT) return R.layout.list_item_text;
        if (type == VNDetailsElement.TYPE_IMAGES) return R.layout.list_item_images;
        if (type == VNDetailsElement.TYPE_TEXT_IMAGES) return R.layout.list_item_text_images;
        return -1;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        final int layout = getChildLayout(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
        }

        switch (layout) {
            case R.layout.list_item_text:
                TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
                expandedListTextView.setText(expandedListText);
                break;

            case R.layout.list_item_images:
                final ImageButton expandedListImage = (ImageButton) convertView.findViewById(R.id.expandedListImage);
                ImageLoader.getInstance().displayImage(expandedListText, expandedListImage);
                convertView.setMinimumHeight(400);
                Lightbox.set(context, expandedListImage, expandedListText);
                break;

            case R.layout.list_item_text_images:
                // TODO
                break;
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).getData().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}