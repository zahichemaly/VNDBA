package com.booboot.vndbandroid.adapter.vndetails;

/**
 * Created by od on 18/03/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListListener;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndb.Links;
import com.booboot.vndbandroid.bean.vndb.Tag;
import com.booboot.vndbandroid.factory.CharacterDataFactory;
import com.booboot.vndbandroid.factory.ReleaseDataFactory;
import com.booboot.vndbandroid.factory.TagDataFactory;
import com.booboot.vndbandroid.factory.VNDetailsFactory;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.Pixels;
import com.booboot.vndbandroid.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedHashMap;
import java.util.List;

public class VNExpandableListAdapter extends BaseExpandableListAdapter {
    private VNDetailsActivity activity;
    private List<String> titles;
    private LinkedHashMap<String, VNDetailsElement> vnDetailsElements;

    public VNExpandableListAdapter(VNDetailsActivity activity, List<String> titles, LinkedHashMap<String, VNDetailsElement> vnDetailsElements) {
        this.activity = activity;
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
        String primaryText = (String) getChild(listPosition, expandedListPosition);
        String secondaryText = (String) getRightChild(listPosition, expandedListPosition);
        final int layout = getChildLayout(listPosition);
        final LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);
        ImageView itemLeftImage, itemRightImage;
        int rightImage;

        switch (layout) {
            case R.layout.list_item_text:
                itemLeftImage = (ImageView) convertView.findViewById(R.id.itemLeftImage);
                TextView itemLeftText = (TextView) convertView.findViewById(R.id.itemLeftText);
                TextView itemRightText = (TextView) convertView.findViewById(R.id.itemRightText);
                itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);

                itemLeftText.setTextColor(activity.getResources().getColor(R.color.white));
                primaryText = Utils.convertLink(activity, primaryText);
                itemLeftText.setText(Html.fromHtml(primaryText));
                if (primaryText.contains("</a>"))
                    itemLeftText.setMovementMethod(LinkMovementMethod.getInstance());

                if (secondaryText == null) itemRightText.setVisibility(View.GONE);
                else {
                    itemRightText.setTextColor(activity.getResources().getColor(R.color.white));
                    secondaryText = Utils.convertLink(activity, secondaryText);
                    itemRightText.setText(Html.fromHtml(secondaryText));
                    if (secondaryText.contains("</a>"))
                        itemRightText.setMovementMethod(LinkMovementMethod.getInstance());
                }

                int leftImage;
                if (getElement(listPosition).getPrimaryImages() != null && (leftImage = getElement(listPosition).getPrimaryImages().get(expandedListPosition)) > 0) {
                    itemLeftImage.setImageResource(leftImage);
                } else {
                    itemLeftImage.setVisibility(View.GONE);
                }

                if (getElement(listPosition).getSecondaryImages() != null && (rightImage = getElement(listPosition).getSecondaryImages().get(expandedListPosition)) > 0) {
                    itemRightImage.setImageResource(rightImage);
                } else {
                    itemRightImage.setVisibility(View.GONE);
                }

                String group = (String) getGroup(listPosition);
                if (group.equals(VNDetailsFactory.TITLE_TAGS)) {
                    int tagId = getElement(listPosition).getIds().get(expandedListPosition);
                    if (tagId > 0) {
                        Tag tag = Tag.getTags(activity).get(tagId);
                        if (tag != null) {
                            convertView.setOnClickListener(new DoubleListListener(activity, tag.getName(), TagDataFactory.getData(tag), null));
                        }
                    }
                }
                break;

            case R.layout.list_item_images:
                final ImageButton expandedListImage = (ImageButton) convertView.findViewById(R.id.expandedListImage);
                ImageLoader.getInstance().displayImage(primaryText, expandedListImage);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Pixels.px(100, activity)));
                Lightbox.set(activity, expandedListImage, primaryText);
                break;

            case R.layout.list_item_subtitle:
                itemLeftImage = (ImageView) convertView.findViewById(R.id.iconView);
                itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);
                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);

                if (getElement(listPosition).getUrlImages() == null) {
                    itemLeftImage.setVisibility(View.GONE);
                } else {
                    String url = getElement(listPosition).getUrlImages().get(expandedListPosition);
                    ImageLoader.getInstance().displayImage(url, itemLeftImage);
                    Lightbox.set(activity, itemLeftImage, url);
                }

                if (getElement(listPosition).getSecondaryImages() != null && (rightImage = getElement(listPosition).getSecondaryImages().get(expandedListPosition)) > 0) {
                    itemRightImage.setImageResource(rightImage);
                } else {
                    itemRightImage.setVisibility(View.GONE);
                }

                title.setTextColor(activity.getResources().getColor(R.color.white));
                title.setText(Html.fromHtml(primaryText));

                if (secondaryText == null) {
                    subtitle.setVisibility(View.GONE);
                } else {
                    subtitle.setTextColor(activity.getResources().getColor(R.color.light_gray));
                    subtitle.setText(secondaryText);
                }

                switch ((String) getGroup(listPosition)) {
                    case VNDetailsFactory.TITLE_RELATIONS:
                    case VNDetailsFactory.TITLE_SIMILAR_NOVELS:
                        final int vnId = getElement(listPosition).getIds().get(expandedListPosition);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Cache.openVNDetails(activity, vnId);
                            }
                        });
                        break;

                    case VNDetailsFactory.TITLE_CHARACTERS:
                        int characterId = getElement(listPosition).getIds().get(expandedListPosition);
                        // TODO : use Cache.characters for O(1) instead of O(n) loop?
                        for (Item character : activity.getCharacters()) {
                            if (character.getId() == characterId) {
                                convertView.setOnClickListener(new DoubleListListener(activity, character.getName(), CharacterDataFactory.getData(activity, character), null));
                                break;
                            }
                        }
                        break;

                    case VNDetailsFactory.TITLE_RELEASES:
                        /* Display a flag next to the language */
                        if (getElement(listPosition).getPrimaryImages() != null) {
                            Integer image = getElement(listPosition).getPrimaryImages().get(expandedListPosition);
                            if (image != null) {
                                itemLeftImage.setImageResource(image);
                                itemLeftImage.setMaxWidth(Pixels.px(35, activity));
                                itemLeftImage.setMaxHeight(Pixels.px(40, activity));
                                ViewGroup.LayoutParams layoutParams = itemLeftImage.getLayoutParams();
                                layoutParams.width = Pixels.px(35, activity);
                                layoutParams.height = Pixels.px(40, activity);
                                itemLeftImage.setLayoutParams(layoutParams);
                                itemLeftImage.setVisibility(View.VISIBLE);
                            }
                        }

                        /* Retrieve the release matching the element */
                        final Integer releaseId = getElement(listPosition).getIds().get(expandedListPosition);
                        if (releaseId == null) break;
                        Item release = null;
                        for (Item tmp : Cache.releases.get(activity.getVn().getId())) {
                            if (tmp.getId() == releaseId) {
                                release = tmp;
                                break;
                            }
                        }
                        if (release == null) break;

                        convertView.setOnClickListener(new DoubleListListener(activity, release.getTitle(), ReleaseDataFactory.getData(release), null));
                        break;

                    case VNDetailsFactory.TITLE_ANIME:
                        final int id = getElement(listPosition).getIds().get(expandedListPosition);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.openURL(activity, Links.ANIDB + id);
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
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        listTitleTextView.setTextColor(activity.getResources().getColor(R.color.white));
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