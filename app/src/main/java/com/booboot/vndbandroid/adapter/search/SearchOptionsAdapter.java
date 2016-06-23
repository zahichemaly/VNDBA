package com.booboot.vndbandroid.adapter.search;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNSearchActivity;
import com.booboot.vndbandroid.api.bean.Tag;
import com.booboot.vndbandroid.view.TagAutoCompleteView;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by od on 21/06/2016.
 */
public class SearchOptionsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private Bundle savedInstanceState;
    private TagAutoCompleteView includeTagsInput;
    private TagAutoCompleteView excludeTagsInput;

    public SearchOptionsAdapter(Context context, Bundle savedInstanceState) {
        this.context = context;
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return 1;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return listPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.vn_search_options, null);

            includeTagsInput = (TagAutoCompleteView) convertView.findViewById(R.id.includeTagsInput);
            excludeTagsInput = (TagAutoCompleteView) convertView.findViewById(R.id.excludeTagsInput);
            initCompletionView(includeTagsInput, VNSearchActivity.INCLUDE_TAGS_STATE);
            initCompletionView(excludeTagsInput, VNSearchActivity.EXCLUDE_TAGS_STATE);
        }

        return convertView;
    }

    private void initCompletionView(final TagAutoCompleteView tagsInput, String tagsState) {
        tagsInput.allowCollapse(false);
        tagsInput.allowDuplicates(false);
        tagsInput.performBestGuess(false);
        ArrayAdapter<Tag> adapter = new FilteredArrayAdapter<Tag>(context, android.R.layout.simple_list_item_1, Tag.getTagsArray(context)) {
            @Override
            protected boolean keepObject(Tag obj, String mask) {
                for (String part : mask.toLowerCase().split(" ")) {
                    if (!obj.getName().toLowerCase().contains(part))
                        return false;
                }
                return true;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (parent instanceof ListView) {
                    ((ListView) parent).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("D", "on item long click !");
                            // TODO display tag's description
                            return true;
                        }
                    });
                }
                return super.getView(position, convertView, parent);
            }
        };
        tagsInput.setAdapter(adapter);
        tagsInput.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);

        tagsInput.setTokenListener(new TokenCompleteTextView.TokenListener<Tag>() {
            @Override
            public void onTokenAdded(Tag token) {
            }

            @Override
            public void onTokenRemoved(Tag token) {
            }
        });

        if (savedInstanceState != null) {
            Parcelable savedState = savedInstanceState.getParcelable(tagsState);
            if (savedState != null)
                tagsInput.onRestoreInstanceState(savedState);
        }
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
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
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText("Search options");
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        // [TODO] return Preferences.copyToClipboardOnLongClick
        return false;
    }

    public TagAutoCompleteView getIncludeTagsInput() {
        return includeTagsInput;
    }

    public TagAutoCompleteView getExcludeTagsInput() {
        return excludeTagsInput;
    }
}
