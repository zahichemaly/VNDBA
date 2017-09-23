package com.booboot.vndbandroid.adapter.doublelist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 26/04/2016.
 */
public class DoubleListListener implements View.OnClickListener {
    private Context context;
    private String title;
    private DoubleListElement[] elements;
    private Callback onDismissCallback;

    public DoubleListListener(Context context, String title, DoubleListElement[] elements, Callback onDismissCallback) {
        this.context = context;
        this.title = title;
        this.elements = elements;
        this.onDismissCallback = onDismissCallback;
    }

    @Override
    public void onClick(View v) {
        createInfoDialog(context, title, new DoubleListAdapter(context, elements), onDismissCallback);
    }

    public static void createInfoDialog(Context context, String title, ListAdapter adapter, final Callback onDismissCallback) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.vn_info_dialog, null);

        ColorStateList buttonBackgroundColor = ColorStateList.valueOf(Utils.getThemeColor(context, R.attr.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById(R.id.closeButton).setBackgroundTintList(buttonBackgroundColor);
        } else {
            ViewCompat.setBackgroundTintList(view.findViewById(R.id.closeButton), buttonBackgroundColor);
        }

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDismissCallback != null) {
                    onDismissCallback.call();
                }
            }
        });
    }
}
