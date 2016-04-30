package com.booboot.vndbandroid.adapter.vndetails;

/**
 * Created by od on 18/03/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.activity.VNTypeFragment;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListListener;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.db.Cache;
import com.booboot.vndbandroid.factory.CharacterDataFactory;
import com.booboot.vndbandroid.factory.ReleaseDataFactory;
import com.booboot.vndbandroid.factory.VNDetailsFactory;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.Pixels;
import com.booboot.vndbandroid.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedHashMap;
import java.util.List;

public class VNExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> titles;
    private LinkedHashMap<String, VNDetailsElement> vnDetailsElements;

    public VNExpandableListAdapter(Context context, List<String> titles, LinkedHashMap<String, VNDetailsElement> vnDetailsElements) {
        this.context = context;
        this.titles = titles;
        this.vnDetailsElements = vnDetailsElements;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return getElement(listPosition).getPrimaryData().get(expandedListPosition);
    }

    private VNDetailsElement getElement(int listPosition) {
        return vnDetailsElements.get(getGroup(listPosition));
    }

    private Object getRightChild(int listPosition, int expandedListPosition) {
        List<String> rightData = getElement(listPosition).getSecondaryData();
        if (rightData == null || rightData.size() <= expandedListPosition) return null;
        return rightData.get(expandedListPosition);
    }

    public int getChildLayout(int listPosition) {
        int type = vnDetailsElements.get(getGroup(listPosition)).getType();
        if (type == VNDetailsElement.TYPE_TEXT) return R.layout.list_item_text;
        if (type == VNDetailsElement.TYPE_IMAGES) return R.layout.list_item_images;
        if (type == VNDetailsElement.TYPE_CUSTOM) return R.layout.list_item_custom;
        if (type == VNDetailsElement.TYPE_SUBTITLE) return R.layout.list_item_subtitle;
        return -1;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String primaryText = (String) getChild(listPosition, expandedListPosition);
        final String secondaryText = (String) getRightChild(listPosition, expandedListPosition);
        final int layout = getChildLayout(listPosition);
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);

        switch (layout) {
            case R.layout.list_item_text:
                ImageView itemLeftImage = (ImageView) convertView.findViewById(R.id.itemLeftImage);
                TextView itemLeftText = (TextView) convertView.findViewById(R.id.itemLeftText);
                TextView itemRightText = (TextView) convertView.findViewById(R.id.itemRightText);
                ImageView itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);

                itemLeftText.setText(Html.fromHtml(primaryText));

                if (secondaryText == null) itemRightText.setVisibility(View.GONE);
                else {
                    if (secondaryText.contains("</a>"))
                        itemRightText.setMovementMethod(LinkMovementMethod.getInstance());
                    itemRightText.setText(Html.fromHtml(secondaryText));
                }

                int leftImage;
                if (getElement(listPosition).getPrimaryImages() != null && (leftImage = getElement(listPosition).getPrimaryImages().get(expandedListPosition)) > 0) {
                    itemLeftImage.setImageResource(leftImage);
                } else {
                    itemLeftImage.setVisibility(View.GONE);
                }

                int rightImage;
                if (getElement(listPosition).getSecondaryImages() != null && (rightImage = getElement(listPosition).getSecondaryImages().get(expandedListPosition)) > 0) {
                    itemRightImage.setImageResource(rightImage);
                } else {
                    itemRightImage.setVisibility(View.GONE);
                }
                break;

            case R.layout.list_item_images:
                final ImageButton expandedListImage = (ImageButton) convertView.findViewById(R.id.expandedListImage);
                ImageLoader.getInstance().displayImage(primaryText, expandedListImage);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Pixels.px(100, context)));
                Lightbox.set(context, expandedListImage, primaryText);
                break;

            case R.layout.list_item_subtitle:
                ImageView iconView = (ImageView) convertView.findViewById(R.id.iconView);
                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);

                if (getElement(listPosition).getUrlImages() == null) {
                    iconView.setVisibility(View.GONE);
                } else {
                    String url = getElement(listPosition).getUrlImages().get(expandedListPosition);
                    ImageLoader.getInstance().displayImage(url, iconView);
                    Lightbox.set(context, iconView, url);
                }

                title.setText(Html.fromHtml(primaryText));

                if (secondaryText == null) {
                    subtitle.setVisibility(View.GONE);
                } else {
                    subtitle.setText(secondaryText);
                }

                switch ((String) getGroup(listPosition)) {
                    case VNDetailsFactory.TITLE_RELATIONS:
                        final int vnId = getElement(listPosition).getPrimaryImages().get(expandedListPosition);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Cache.vnlist.get(vnId) != null) {
                                    Intent intent = new Intent(context, VNDetailsActivity.class);
                                    intent.putExtra(VNTypeFragment.VN_ARG, Cache.vnlist.get(vnId));
                                    context.startActivity(intent);
                                    return;
                                }

                                VNDBServer.get("vn", Cache.VN_FLAGS, "(id = " + vnId + ")", Options.create(false, false), context, new Callback() {
                                    @Override
                                    protected void config() {
                                        if (!results.getItems().isEmpty()) {
                                            Intent intent = new Intent(context, VNDetailsActivity.class);
                                            intent.putExtra(VNTypeFragment.VN_ARG, results.getItems().get(0));
                                            context.startActivity(intent);
                                        }
                                    }
                                }, Callback.errorCallback(context));
                            }
                        });
                        break;

                    case VNDetailsFactory.TITLE_CHARACTERS:
                        final Item character = ((VNDetailsActivity) context).getCharacters().get(expandedListPosition);
                        convertView.setOnClickListener(new DoubleListListener(context, character.getName(), CharacterDataFactory.getData(context, character)));
                        break;

                    case VNDetailsFactory.TITLE_RELEASES:
                        /* Display a flag next to the language */
                        if (getElement(listPosition).getPrimaryImages() != null) {
                            Integer image = getElement(listPosition).getPrimaryImages().get(expandedListPosition);
                            if (image != null) {
                                iconView.setImageResource(image);
                                iconView.setMaxWidth(Pixels.px(35, context));
                                iconView.setMaxHeight(Pixels.px(40, context));
                                ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
                                layoutParams.width = Pixels.px(35, context);
                                layoutParams.height = Pixels.px(40, context);
                                iconView.setLayoutParams(layoutParams);
                                iconView.setVisibility(View.VISIBLE);
                            }
                        }

                        /* Retrieve the release matching the element */
                        final Integer releaseId = getElement(listPosition).getSecondaryImages().get(expandedListPosition);
                        if (releaseId == null) break;
                        Item release = null;
                        for (Item tmp : Cache.releases.get(((VNDetailsActivity) context).getVn().getId())) {
                            if (tmp.getId() == releaseId) {
                                release = tmp;
                                break;
                            }
                        }
                        if (release == null) break;

                        convertView.setOnClickListener(new DoubleListListener(context, release.getTitle(), ReleaseDataFactory.getData(release)));
                        break;

                    case VNDetailsFactory.TITLE_ANIME:
                        final int id = getElement(listPosition).getPrimaryImages().get(expandedListPosition);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.openInBrowser(context, Links.ANIDB + id);
                            }
                        });
                        break;
                }
                break;
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return vnDetailsElements.get(getGroup(listPosition)).getPrimaryData().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return titles.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        // [TODO] return Preferences.copyToClipboardOnLongClick
        return true;
    }
}