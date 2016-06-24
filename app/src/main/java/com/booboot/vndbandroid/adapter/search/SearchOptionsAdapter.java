package com.booboot.vndbandroid.adapter.search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNSearchActivity;
import com.booboot.vndbandroid.api.bean.Tag;
import com.booboot.vndbandroid.view.TagAutoCompleteView;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

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

            final FloatLabeledEditText includeTagsFloatingLabel = (FloatLabeledEditText) convertView.findViewById(R.id.includeTagsFloatingLabel);
            final ImageView includeTagsIcon = (ImageView) convertView.findViewById(R.id.includeTagsIcon);
            final ImageView includeTagsDropdown = (ImageView) convertView.findViewById(R.id.includeTagsDropdown);
            ImageView excludeTagsIcon = (ImageView) convertView.findViewById(R.id.excludeTagsIcon);
            includeTagsIcon.setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
            includeTagsDropdown.setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
            excludeTagsIcon.setColorFilter(context.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

            LinearLayout includeTagsLayout = (LinearLayout) convertView.findViewById(R.id.includeTagsLayout);
            includeTagsLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    switch (arg1.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            includeTagsIcon.setAlpha(0.9f);
                            includeTagsDropdown.setAlpha(0.9f);
                            break;

                        case MotionEvent.ACTION_UP:
                            includeTagsIcon.setAlpha(0.4f);
                            includeTagsDropdown.setAlpha(0.4f);
                            PopupMenu popup = new PopupMenu(context, includeTagsDropdown);
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.include_tags, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (item.getItemId() == R.id.item_include_all) {
                                        includeTagsInput.setHint(R.string.include_all_tags);
                                        includeTagsFloatingLabel.setHint(context.getResources().getString(R.string.include_all_tags));
                                    } else if (item.getItemId() == R.id.item_include_one) {
                                        includeTagsInput.setHint(R.string.include_one_tags);
                                        includeTagsFloatingLabel.setHint(context.getResources().getString(R.string.include_one_tags));
                                    }
                                    return false;
                                }
                            });
                            popup.show();
                            break;

                        case MotionEvent.ACTION_CANCEL:
                            includeTagsIcon.setAlpha(0.4f);
                            includeTagsDropdown.setAlpha(0.4f);
                            break;
                    }
                    return true;
                }
            });
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
