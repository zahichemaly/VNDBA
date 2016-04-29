package com.booboot.vndbandroid.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
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
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.db.Cache;
import com.booboot.vndbandroid.factory.VNDetailsFactory;
import com.booboot.vndbandroid.listener.VNDetailsListener;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class VNDetailsActivity extends AppCompatActivity {
    public static int spoilerLevel = -1;
    private ActionBar actionBar;
    private Item vn;
    private Item vnlistVn;
    private Item wishlistVn;
    private Item votelistVn;
    private List<Item> characters;
    private LinkedHashMap<String, List<Item>> releases;

    private ImageButton image;
    private Button statusButton;
    private Button wishlistButton;
    private Button votesButton;

    private VNDetailsListener listener;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private VNDetailsElement characterElement;
    private VNDetailsElement releaseElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(VNTypeFragment.VN_ARG);
        listener = new VNDetailsListener(this, vn);

        if (spoilerLevel < 0) {
            if (SettingsManager.getSpoilerCompleted(this) && vn.getStatus() == Status.FINISHED)
                spoilerLevel = 2;
            else
                spoilerLevel = SettingsManager.getSpoilerLevel(this);
        }

        initCharacters();
        initReleases();
        init();
    }

    private void init() {
        vnlistVn = Cache.vnlist.get(vn.getId());
        wishlistVn = Cache.wishlist.get(vn.getId());
        votelistVn = Cache.votelist.get(vn.getId());
        if (Cache.characters.get(vn.getId()) != null) {
            characters = Cache.characters.get(vn.getId());
        }
        if (Cache.releases.get(vn.getId()) != null) {
            groupReleasesByLanguage(Cache.releases.get(vn.getId()));
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

        if (vn.isImage_nsfw() && !SettingsManager.getNSFW(this)) {
            image.setImageResource(R.drawable.ic_nsfw);
        } else {
            ImageLoader.getInstance().displayImage(vn.getImage(), image);
            Lightbox.set(VNDetailsActivity.this, image, vn.getImage());
        }
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
        if (Cache.characters.get(vn.getId()) == null) {
            VNDBServer.get("character", Cache.CHARACTER_FLAGS, "(vn = " + vn.getId() + ")", Options.create(true, true), this, new Callback() {
                @Override
                protected void config() {
                    if (results.getItems().isEmpty()) {
                        characters = Cache.characters.get(vn.getId());
                    } else {
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
                        Cache.characters.put(vn.getId(), characters);
                        Cache.saveToCache(VNDetailsActivity.this, Cache.CHARACTERS_CACHE, Cache.characters);
                    }

                    if (characters == null) return;

                    VNDetailsFactory.CharacterElementWrapper characterElementWrapper = VNDetailsFactory.getCharacters(VNDetailsActivity.this);
                    characterElement.setPrimaryData(characterElementWrapper.character_names);
                    characterElement.setSecondaryData(characterElementWrapper.character_subnames);
                    characterElement.setUrlImages(characterElementWrapper.character_images);
                }
            }, Callback.errorCallback(this));
        }
    }

    private void initReleases() {
        if (Cache.releases.get(vn.getId()) == null) {
            VNDBServer.get("release", Cache.RELEASE_FLAGS, "(vn = " + vn.getId() + ")", Options.create(1, 25, "released", false, true, true), this, new Callback() {
                @Override
                protected void config() {
                    List<Item> releasesList;
                    if (results.getItems().isEmpty()) {
                        releasesList = Cache.releases.get(vn.getId());
                    } else {
                        releasesList = results.getItems();
                        Cache.releases.put(vn.getId(), releasesList);
                        Cache.saveToCache(VNDetailsActivity.this, Cache.RELEASES_CACHE, Cache.releases);
                    }

                    if (releasesList == null) return;
                    groupReleasesByLanguage(releasesList);

                    VNDetailsFactory.ReleaseElementWrapper releaseElementWrapper = VNDetailsFactory.getReleases(VNDetailsActivity.this);
                    releaseElement.setPrimaryImages(releaseElementWrapper.release_images);
                    releaseElement.setPrimaryData(releaseElementWrapper.release_names);
                    releaseElement.setSecondaryData(releaseElementWrapper.release_subnames);
                    releaseElement.setSecondaryImages(releaseElementWrapper.release_ids);
                }
            }, Callback.errorCallback(this));
        }
    }

    /**
     * Cache.releases just gives the set of all the releases for the VN.
     * That's not what we want though : like it is displayed on VNDB.org, we want to group these
     * VNs by language. That means that we somehow want a data structure "Language" -> "List of releases"
     * in which a release may appear several times for different languages!
     *
     * @param releasesList the releases list for the VN, where each release appear a single time.
     */
    private void groupReleasesByLanguage(List<Item> releasesList) {
        releases = new LinkedHashMap<>();
        for (Item release : releasesList) {
            for (String language : release.getLanguages()) {
                if (releases.get(language) == null)
                    releases.put(language, new ArrayList<Item>());
                releases.get(language).add(release);
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vn_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /* Refreshing tabs' counter upon leaving (may have made changes in this activity) */
                if (MainActivity.instance.getVnlistFragment() != null) MainActivity.instance.getVnlistFragment().refreshTitles();
                Cache.saveToCache(this, Cache.VNLIST_CACHE, Cache.vnlist);
                Cache.saveToCache(this, Cache.VOTELIST_CACHE, Cache.votelist);
                Cache.saveToCache(this, Cache.WISHLIST_CACHE, Cache.wishlist);
                finish();
                break;

            case R.id.action_view_on_vndb:
                Utils.openInBrowser(this, Links.VNDB_PAGE + vn.getId());
                break;

            case R.id.action_spoiler:
                PopupMenu popup = new PopupMenu(this, findViewById(R.id.action_spoiler));
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.spoiler, popup.getMenu());
                switch (spoilerLevel) {
                    case 1:
                        popup.getMenu().findItem(R.id.item_spoil_1).setChecked(true);
                        break;
                    case 2:
                        popup.getMenu().findItem(R.id.item_spoil_2).setChecked(true);
                        break;
                    default:
                        popup.getMenu().findItem(R.id.item_spoil_0).setChecked(true);
                        break;
                }
                popup.setOnMenuItemClickListener(listener);
                listener.setPopupButton(null);
                popup.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCharacterElement(VNDetailsElement characterElement) {
        this.characterElement = characterElement;
    }

    public VNDetailsElement getReleaseElement() {
        return releaseElement;
    }

    public void setReleaseElement(VNDetailsElement releaseElement) {
        this.releaseElement = releaseElement;
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

    public LinkedHashMap<String, List<Item>> getReleases() {
        return releases;
    }
}
