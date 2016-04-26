package com.booboot.vndbandroid.adapter.doublelist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.MainActivity;

/**
 * Created by od on 26/04/2016.
 */
public class DoubleListListener implements View.OnClickListener {
    private Context context;
    private String title;
    private DoubleListElement[] elements;

    public DoubleListListener(Context context, String title, DoubleListElement[] elements) {
        this.context = context;
        this.title = title;
        this.elements = elements;
    }

    @Override
    public void onClick(View v) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.vn_info_dialog, null);
        view.findViewById(R.id.closeButton).setBackgroundTintList(ColorStateList.valueOf(MainActivity.getThemeColor(context, R.attr.colorPrimaryDark)));

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new DoubleListAdapter(context, elements));

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(title);
        dialogBuilder.setCancelable(true);
        final AlertDialog dialog = dialogBuilder.show();
        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
