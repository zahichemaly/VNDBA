package com.booboot.vndbandroid.adapter.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListListener;
import com.booboot.vndbandroid.model.vndb.Tag;
import com.booboot.vndbandroid.factory.TagDataFactory;
import com.booboot.vndbandroid.util.Callback;
import com.tokenautocomplete.FilteredArrayAdapter;

/**
 * Created by od on 29/06/2016.
 */
public class TagFilteredArrayAdapter extends FilteredArrayAdapter<Tag> {
    private int layout;
    private TagAutoCompleteView completionView;

    public TagFilteredArrayAdapter(Context context, int resource, Tag[] objects, TagAutoCompleteView completionView) {
        super(context, resource, objects);
        this.layout = resource;
        this.completionView = completionView;
    }

    @Override
    protected boolean keepObject(Tag obj, String mask) {
        for (String part : mask.toLowerCase().split(" ")) {
            if (!obj.getName().toLowerCase().contains(part))
                return false;
        }
        return true;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, parent, false);
        } else {
            view = convertView;
        }

        Tag tag = getItem(position);
        if (tag != null) {
            TextView text = view.findViewById(R.id.tokenAutoCompleteText);
            ImageView tokenAutoCompleteInfo = view.findViewById(R.id.tokenAutoCompleteInfo);
            text.setText(tag.getName());
            tokenAutoCompleteInfo.setOnClickListener(new DoubleListListener(getContext(), tag.getName(), TagDataFactory.getData(tag), new Callback() {
                @Override
                protected void config() {
                    completionView.showDropDown();
                }
            }));
        }

        return view;
    }
}
