package com.booboot.vndbandroid.adapter.search;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNSearchActivity;

/**
 * Created by od on 21/06/2016.
 */
public class SearchOptionsAdapter extends BaseExpandableListAdapter {
    private VNSearchActivity context;

    public SearchOptionsAdapter(VNSearchActivity context) {
        this.context = context;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return 0;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return listPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int listPosition) {
        return 1;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_search_options, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText("Search options");
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        context.searchOptionsLayout.setVisibility(View.GONE);
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        context.searchOptionsLayout.setVisibility(View.VISIBLE);
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return false;
    }
}
