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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.Pixels;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.LinkedHashMap;
import java.util.List;

public class VNExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, VNDetailsElement> expandableListDetail;

    public VNExpandableListAdapter(Context context, List<String> expandableListTitle, LinkedHashMap<String, VNDetailsElement> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return expandableListDetail.get(expandableListTitle.get(listPosition)).getLeftData().get(expandedListPosition);
    }

    public List<Integer> getLeftImages(int listPosition) {
        return expandableListDetail.get(expandableListTitle.get(listPosition)).getLeftImages();
    }

    public List<Integer> getRightImages(int listPosition) {
        return expandableListDetail.get(expandableListTitle.get(listPosition)).getRightImages();
    }

    public Object getRightChild(int listPosition, int expandedListPosition) {
        List<String> rightData = expandableListDetail.get(expandableListTitle.get(listPosition)).getRightData();
        if (rightData == null || rightData.size() <= expandedListPosition) return null;
        return rightData.get(expandedListPosition);
    }

    public int getChildLayout(int listPosition) {
        int type = expandableListDetail.get(expandableListTitle.get(listPosition)).getType();
        if (type == VNDetailsElement.TYPE_TEXT) return R.layout.list_item_text;
        if (type == VNDetailsElement.TYPE_IMAGES) return R.layout.list_item_images;
        if (type == VNDetailsElement.TYPE_CUSTOM) return R.layout.list_item_custom;
        return -1;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String leftText = (String) getChild(listPosition, expandedListPosition);
        final String rightText = (String) getRightChild(listPosition, expandedListPosition);
        final int layout = getChildLayout(listPosition);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);

        switch (layout) {
            case R.layout.list_item_text:
                ImageView itemLeftImage = (ImageView) convertView.findViewById(R.id.itemLeftImage);
                TextView itemLeftText = (TextView) convertView.findViewById(R.id.itemLeftText);
                TextView itemRightText = (TextView) convertView.findViewById(R.id.itemRightText);
                ImageView itemRightImage = (ImageView) convertView.findViewById(R.id.itemRightImage);

                itemLeftText.setText(Html.fromHtml(leftText));

                if (rightText == null) itemRightText.setVisibility(View.GONE);
                else {
                    if (rightText.contains("</a>"))
                        itemRightText.setMovementMethod(LinkMovementMethod.getInstance());
                    itemRightText.setText(Html.fromHtml(rightText));
                }

                int leftImage;
                if (getLeftImages(listPosition) != null && (leftImage = getLeftImages(listPosition).get(expandedListPosition)) > 0) {
                    itemLeftImage.setImageResource(leftImage);
                } else {
                    itemLeftImage.setVisibility(View.GONE);
                }

                int rightImage;
                if (getRightImages(listPosition) != null && (rightImage = getRightImages(listPosition).get(expandedListPosition)) > 0) {
                    itemRightImage.setImageResource(rightImage);
                } else {
                    itemRightImage.setVisibility(View.GONE);
                }
                break;

            case R.layout.list_item_images:
                final ImageButton expandedListImage = (ImageButton) convertView.findViewById(R.id.expandedListImage);
                ImageLoader.getInstance().displayImage(leftText, expandedListImage);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Pixels.px(100, context)));
                Lightbox.set(context, expandedListImage, leftText);
                break;

            case R.layout.list_item_custom:
                TreeNode root = TreeNode.root();

                TreeNode myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "My Profile")).setViewHolder(new ProfileHolder(context));
                TreeNode bruce = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Bruce Wayne")).setViewHolder(new ProfileHolder(context));
                TreeNode clark = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Clark Kent")).setViewHolder(new ProfileHolder(context));
                TreeNode barry = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Barry Allen")).setViewHolder(new ProfileHolder(context));
                addProfileData(myProfile);
                addProfileData(clark);
                addProfileData(bruce);
                addProfileData(barry);
                root.addChildren(myProfile, bruce, barry, clark);

                AndroidTreeView tView = new AndroidTreeView(context, root);
                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
                ((LinearLayout)convertView).addView(tView.getView());
                break;
        }

        return convertView;
    }

    private void addProfileData(TreeNode profile) {
        TreeNode socialNetworks = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, "Social")).setViewHolder(new HeaderHolder(context));
        TreeNode places = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_place, "Places")).setViewHolder(new HeaderHolder(context));

        TreeNode facebook = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_facebook)).setViewHolder(new SocialViewHolder(context));
        TreeNode linkedin = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_linkedin)).setViewHolder(new SocialViewHolder(context));
        TreeNode google = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_gplus)).setViewHolder(new SocialViewHolder(context));
        TreeNode twitter = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_twitter)).setViewHolder(new SocialViewHolder(context));

        TreeNode lake = new TreeNode(new PlaceHolderHolder.PlaceItem("A rose garden")).setViewHolder(new PlaceHolderHolder(context));
        TreeNode mountains = new TreeNode(new PlaceHolderHolder.PlaceItem("The white house")).setViewHolder(new PlaceHolderHolder(context));

        TreeNode lake2 = new TreeNode(new PlaceHolderHolder.PlaceItem("A rose fsdfsdfsdf")).setViewHolder(new PlaceHolderHolder(context));

        lake.addChildren(lake2);
        places.addChildren(lake, mountains);
        socialNetworks.addChildren(facebook, google, twitter, linkedin);
        profile.addChildren(socialNetworks, places);
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return expandableListDetail.get(expandableListTitle.get(listPosition)).getLeftData().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
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