package com.booboot.vndbandroid.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vndetails.VNDetailsElement;
import com.booboot.vndbandroid.adapter.vndetails.VNExpandableListAdapter;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.db.DB;
import com.booboot.vndbandroid.factory.VNDetailsFactory;
import com.booboot.vndbandroid.listener.VNDetailsListener;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.SettingsManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class VNDetailsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Item vn;
    private Item vnlistVn;
    private Item wishlistVn;
    private Item votelistVn;
    private List<Item> characters;

    private ImageButton image;
    private Button statusButton;
    private Button wishlistButton;
    private Button votesButton;

    private VNDetailsListener listener;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private VNDetailsElement characterElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(VNTypeFragment.VN_ARG);
        listener = new VNDetailsListener(this, vn);

        initCharacters();
        init();
    }

    private void init() {
        vnlistVn = DB.vnlist.get(vn.getId());
        wishlistVn = DB.wishlist.get(vn.getId());
        votelistVn = DB.votelist.get(vn.getId());
        if (DB.characters.get(vn.getId()) != null) {
            characters = DB.characters.get(vn.getId());
        }

        initExpandableListView();

        image = (ImageButton) findViewById(R.id.image);
        statusButton = (Button) findViewById(R.id.statusButton);
        wishlistButton = (Button) findViewById(R.id.wishlistButton);
        votesButton = (Button) findViewById(R.id.votesButton);

        actionBar = getSupportActionBar();
        actionBar.setTitle(vn.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        statusButton.setText(Status.toString(vnlistVn != null ? vnlistVn.getStatus() : -1));
        wishlistButton.setText(Priority.toString(wishlistVn != null ? wishlistVn.getPriority() : -1));
        votesButton.setText(Vote.toString(votelistVn != null ? votelistVn.getVote() : -1));

        statusButton.setBackgroundTintList(ColorStateList.valueOf(MainActivity.getThemeColor(this, R.attr.colorPrimaryDark)));
        wishlistButton.setBackgroundTintList(ColorStateList.valueOf(MainActivity.getThemeColor(this, R.attr.colorPrimaryDark)));
        votesButton.setBackgroundTintList(ColorStateList.valueOf(MainActivity.getThemeColor(this, R.attr.colorPrimaryDark)));

        ImageLoader.getInstance().displayImage(vn.getImage(), image);
        Lightbox.set(VNDetailsActivity.this, image, vn.getImage());
    }

    private void initExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = VNDetailsFactory.getData(this);
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new VNExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.addHeaderView(getLayoutInflater().inflate(R.layout.vn_details_header, null));
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() == R.id.list_item_text_layout) {
                    TextView itemLeftText = (TextView) view.findViewById(R.id.itemLeftText);
                    TextView itemRightText = (TextView) view.findViewById(R.id.itemRightText);

                    String copiedText = itemLeftText.getText().toString();
                    if (!itemRightText.getText().toString().isEmpty())
                        copiedText += " : " + itemRightText.getText().toString();

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("CLIPBOARD", copiedText);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(VNDetailsActivity.this, "Element copied to clipboard.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        /* Disables click on certain elements if they have finished loading */
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.getExpandableListAdapter().getChildrenCount(groupPosition) < 1) {
                    Toast.makeText(VNDetailsActivity.this, "Nothing to show here yet...", Toast.LENGTH_SHORT).show();
                    return true;
                } else
                    return false;
            }
        });
    }

    private void initCharacters() {
        if (DB.characters.get(vn.getId()) == null) {
            VNDBServer.get("character", DB.CHARACTER_FLAGS, "(vn = " + vn.getId() + ")", Options.create(1, 25, null, false), this, new Callback() {
                @Override
                protected void config() {
                    characters = results.getItems();
                     /* Sorting the characters with their role (Protagonist then main characters etc.) */
                    Collections.sort(characters, new Comparator<Item>() {
                        @Override
                        public int compare(Item lhs, Item rhs) {
                            if (lhs.getVns() == null || lhs.getVns().size() < 1 || lhs.getVns().get(0).length < 1 || rhs.getVns() == null || rhs.getVns().size() < 1 || rhs.getVns().get(0).length < 1)
                                return 0;
                            String leftRole = (String) lhs.getVns().get(0)[Item.ROLE_INDEX];
                            String rightRole = (String) rhs.getVns().get(0)[Item.ROLE_INDEX];
                            return Integer.valueOf(Item.ROLES_KEY.indexOf(leftRole)).compareTo(Item.ROLES_KEY.indexOf(rightRole));
                        }
                    });
                    DB.characters.put(vn.getId(), characters);

                    HashMap<String, List<String>> characters = VNDetailsFactory.getCharacters(VNDetailsActivity.this);
                    characterElement.setPrimaryData(characters.get("character_names"));
                    characterElement.setSecondaryData(characters.get("character_subnames"));
                    characterElement.setUrlImages(characters.get("character_images"));
                }
            }, Callback.errorCallback(this));
        }
    }

    public void showStatusPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.status, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        listener.setPopupButton(statusButton);
        popup.show();
    }

    public void showWishlistPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.wishlist, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        listener.setPopupButton(wishlistButton);
        popup.show();
    }

    public void showVotesPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.votes, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        listener.setPopupButton(votesButton);
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /* Refreshing tabs' counter upon leaving (may have made changes in this activity) */
                MainActivity.instance.getVnlistFragment().refreshTitles();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCharacterElement(VNDetailsElement characterElement) {
        this.characterElement = characterElement;
    }

    public VNDetailsElement getCharacterElement() {
        return characterElement;
    }

    public Item getVn() {
        return vn;
    }

    public List<Item> getCharacters() {
        return characters;
    }
}
