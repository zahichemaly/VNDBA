package com.booboot.vndbandroid.adapter.vndetails.treeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Lightbox;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Bogdan Melnychuk on 2/15/15, modified by Szigeti Peter 2/2/16.
 */
public class ArrowExpandSelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<ArrowExpandSelectableHeaderHolder.IconTreeItem> {
    private TextView title;
    private ImageView arrowView;

    public ArrowExpandSelectableHeaderHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, ArrowExpandSelectableHeaderHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_node, null, false);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(value.text);

        final ImageView iconView = (ImageView) view.findViewById(R.id.iconView);
        ImageLoader.getInstance().displayImage(value.icon, iconView);
        Lightbox.set(context, iconView, value.icon);

        arrowView = (ImageView) view.findViewById(R.id.arrowView);
        arrowView.setPadding(20, 10, 10, 10);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.GONE);
        }
        arrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tView.toggleNode(node);
            }
        });

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setImageResource(active ? R.drawable.ic_keyboard_arrow_down : R.drawable.ic_keyboard_arrow_right);
    }

    public static class IconTreeItem {
        public String icon;
        public String text;

        public IconTreeItem(String icon, String text) {
            this.icon = icon;
            this.text = text;
        }
    }
}
