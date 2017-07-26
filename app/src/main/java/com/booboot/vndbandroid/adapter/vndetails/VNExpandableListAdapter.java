package com.booboot.vndbandroid.adapter.vndetails;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import com.booboot.vndbandroid.adapter.doublelist.DoubleListElement;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListListener;
import com.booboot.vndbandroid.adapter.doublelist.SubtitleAdapter;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.factory.CharacterDataFactory;
import com.booboot.vndbandroid.factory.ReleaseDataFactory;
import com.booboot.vndbandroid.factory.TagDataFactory;
import com.booboot.vndbandroid.factory.VNDetailsFactory;
import com.booboot.vndbandroid.model.vndb.Character;
import com.booboot.vndbandroid.model.vndb.CharacterVoiced;
import com.booboot.vndbandroid.model.vndb.Links;
import com.booboot.vndbandroid.model.vndb.Release;
import com.booboot.vndbandroid.model.vndb.Staff;
import com.booboot.vndbandroid.model.vndb.Tag;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.Utils;
import com.booboot.vndbandroid.util.image.BlurIfDemoTransform;
import com.booboot.vndbandroid.util.image.Pixels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        return getElement(listPosition).getData().get(expandedListPosition).text1;
    }

    private VNDetailsElement getElement(int listPosition) {
        return vnDetailsElements.get(getGroup(listPosition));
    }

    private Object getRightChild(int listPosition, int expandedListPosition) {
        List<VNDetailsElement.Data> data = getElement(listPosition).getData();
        if (data.size() <= expandedListPosition) return null;
        return data.get(expandedListPosition).text2;
    }

    private int getChildLayout(int listPosition) {
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
        final VNDetailsElement element = getElement(listPosition);

        if (expandedListPosition >= element.getData().size()) return convertView;
        final VNDetailsElement.Data elementData = element.getData().get(expandedListPosition);

        switch (layout) {
            case R.layout.list_item_text:
                itemLeftImage = (ImageView) convertView.findViewById(R.id.itemLeftImage);
                TextView itemLeftText = (TextView) convertView.findViewById(R.id.itemLeftText);
                TextView itemRightText = (TextView) convertView.findViewById(R.id.itemRightText);
                itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);

                itemLeftText.setTextColor(Utils.getTextColorFromBackground(activity, R.color.primaryText, R.color.white, activity.isNsfw()));
                primaryText = Utils.convertLink(activity, primaryText);
                itemLeftText.setText(Html.fromHtml(primaryText));
                if (primaryText.contains("</a>"))
                    itemLeftText.setMovementMethod(LinkMovementMethod.getInstance());

                if (secondaryText == null) itemRightText.setVisibility(View.GONE);
                else {
                    itemRightText.setTextColor(Utils.getTextColorFromBackground(activity, R.color.primaryText, R.color.white, activity.isNsfw()));
                    secondaryText = Utils.convertLink(activity, secondaryText);
                    itemRightText.setText(Html.fromHtml(secondaryText));
                    if (secondaryText.contains("</a>"))
                        itemRightText.setMovementMethod(LinkMovementMethod.getInstance());
                }

                if (elementData.image1 > 0) {
                    itemLeftImage.setImageResource(elementData.image1);
                } else {
                    itemLeftImage.setVisibility(View.GONE);
                }

                if (elementData.image2 > 0) {
                    itemRightImage.setImageResource(elementData.image2);
                } else {
                    itemRightImage.setVisibility(View.GONE);
                }

                if (getGroup(listPosition).equals(VNDetailsFactory.TITLE_TAGS)) {
                    if (elementData.id > 0) {
                        Tag tag = Tag.getTags(activity).get(elementData.id);
                        if (tag != null) {
                            convertView.setOnClickListener(new DoubleListListener(activity, tag.getName(), TagDataFactory.getData(tag), null));
                        }
                    }
                }
                break;

            case R.layout.list_item_images:
                final ImageButton expandedListImage = (ImageButton) convertView.findViewById(R.id.expandedListImage);
                Picasso.with(activity).load(primaryText).transform(new BlurIfDemoTransform(activity)).into(expandedListImage);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Pixels.px(100, activity)));
                Lightbox.set(activity, expandedListImage, primaryText);
                break;

            case R.layout.list_item_subtitle:
                itemLeftImage = (ImageView) convertView.findViewById(R.id.iconView);
                itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);
                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView subtitle = (TextView) convertView.findViewById(R.id.subtitle);
                ImageButton itemButton = (ImageButton) convertView.findViewById(R.id.itemButton);
                boolean hasLeftImage = false;

                if (elementData.urlImage != null) {
                    Picasso.with(activity).load(elementData.urlImage).transform(new BlurIfDemoTransform(activity)).into(itemLeftImage);
                    Lightbox.set(activity, itemLeftImage, elementData.urlImage);
                    hasLeftImage = true;
                }

                if (elementData.image1 > 0) {
                    itemLeftImage.setImageResource(elementData.image1);
                    if (elementData.tintImage1) {
                        Utils.tintImageFromBackground(activity, itemLeftImage, R.color.primaryText, activity.isNsfw());
                    }
                    hasLeftImage = true;
                }

                if (!hasLeftImage) itemLeftImage.setVisibility(View.GONE);

                if (elementData.image2 > 0) {
                    itemRightImage.setImageResource(elementData.image2);
                } else {
                    itemRightImage.setVisibility(View.GONE);
                }

                if (elementData.button > 0) {
                    itemButton.setImageResource(elementData.button);
                } else {
                    itemButton.setVisibility(View.GONE);
                }

                title.setTextColor(Utils.getTextColorFromBackground(activity, R.color.primaryText, R.color.white, activity.isNsfw()));
                title.setText(Html.fromHtml(primaryText));

                if (secondaryText == null) {
                    subtitle.setVisibility(View.GONE);
                } else {
                    subtitle.setTextColor(Utils.getTextColorFromBackground(activity, R.color.secondaryText, R.color.light_gray, activity.isNsfw()));
                    subtitle.setText(secondaryText);
                }

                switch (getGroup(listPosition)) {
                    case VNDetailsFactory.TITLE_RELATIONS:
                    case VNDetailsFactory.TITLE_SIMILAR_NOVELS:
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Cache.openVNDetails(activity, elementData.id);
                            }
                        });
                        break;

                    case VNDetailsFactory.TITLE_CHARACTERS:
                        for (final Character character : activity.getCharacters()) {
                            if (character.getId() == elementData.id) {
                                convertView.setOnClickListener(new DoubleListListener(activity, character.getName(), CharacterDataFactory.getData(activity, character), null));

                                if (character.getVoiced().isEmpty()) {
                                    itemButton.setVisibility(View.GONE);
                                } else {
                                    itemButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final List<CharacterVoiced> filteredVoiced = new ArrayList<>();
                                            for (CharacterVoiced voiced : character.getVoiced())
                                                if (voiced.getVid() == activity.getVn().getId())
                                                    filteredVoiced.add(voiced);

                                            Log.e("D", "STAFF ASSOCIATED : " + filteredVoiced);

                                            // TODO show the voiced list in a modal
                                            Cache.getStaff(activity, activity.getVn().getId(), activity.getCharacters(), filteredVoiced, new Callback() {
                                                @Override
                                                protected void config() {
                                                    List<DoubleListElement> voicedElements = new ArrayList<>(filteredVoiced.size());
                                                    for (CharacterVoiced voiced : filteredVoiced) {
                                                        Staff staff = Cache.staff.get(voiced.getId());
                                                        voicedElements.add(new DoubleListElement(staff.getName() + "", voiced.getNote(), false));
                                                    }

                                                    DoubleListListener.createInfoDialog(activity, character.getName() + " is voiced by...", new SubtitleAdapter(activity, voicedElements), null);
                                                }
                                            });
                                        }
                                    });

                                    itemButton.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }
                        break;

                    case VNDetailsFactory.TITLE_STAFF:
                        /* Display an icon next to the name */
                        if (itemLeftImage.getVisibility() == View.VISIBLE) {
                            ViewGroup.LayoutParams layoutParams = itemLeftImage.getLayoutParams();
                            layoutParams.width = Pixels.px(25, activity);
                            layoutParams.height = Pixels.px(25, activity);
                            itemLeftImage.setLayoutParams(layoutParams);
                        }
                        break;

                    case VNDetailsFactory.TITLE_RELEASES:
                        /* Display a flag next to the language */
                        if (itemLeftImage.getVisibility() == View.VISIBLE) {
                            ViewGroup.LayoutParams layoutParams = itemLeftImage.getLayoutParams();
                            layoutParams.width = Pixels.px(35, activity);
                            layoutParams.height = Pixels.px(40, activity);
                            itemLeftImage.setLayoutParams(layoutParams);
                        }

                        /* Retrieve the release matching the element */
                        if (elementData.id < 0) break;
                        Release release = null;
                        for (Release tmp : Cache.releases.get(activity.getVn().getId())) {
                            if (tmp.getId() == elementData.id) {
                                release = tmp;
                                break;
                            }
                        }
                        if (release == null) break;

                        convertView.setOnClickListener(new DoubleListListener(activity, release.getTitle(), ReleaseDataFactory.getData(release), null));
                        break;

                    case VNDetailsFactory.TITLE_ANIME:
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.openURL(activity, Links.ANIDB + elementData.id);
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
        return vnDetailsElements.get(getGroup(listPosition)).getData().size();
    }

    @Override
    public String getGroup(int listPosition) {
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
        String listTitle = getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        listTitleTextView.setTextColor(Utils.getTextColorFromBackground(activity, R.color.primaryText, R.color.white, activity.isNsfw()));
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