package com.booboot.vndbandroid.adapter.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.model.vndb.Staff;

import java.util.List;

/**
 * Created by od on 06/04/2016.
 */
public class StaffAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<Staff> staffs;

    public StaffAdapter(Context context, List<Staff> staffs) {
        this.context = context;
        this.staffs = staffs;
    }

    @Override
    public int getCount() {
        return staffs.size();
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
            convertView = layoutInflater.inflate(R.layout.staff_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
        ImageButton itemButton = (ImageButton) convertView.findViewById(R.id.itemButton);

        final Staff staff = staffs.get(position);

        title.setText(staff.getName());
        if (staff.getNote() != null && !staff.getNote().isEmpty()) {
            subtitle.setText(staff.getNote());
        } else {
            subtitle.setText(R.string.voice_actor);
        }

        convertView.setTag(staff.getId());
        itemButton.setTag(staff.getId());
        convertView.setOnClickListener(this);
        itemButton.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.staffItem:
            case R.id.itemButton:
                Cache.openStaff(context, (int) v.getTag());
                break;
        }
    }
}