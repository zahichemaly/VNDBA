package com.booboot.vndbandroid.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vndetails.VNDetailsElement;
import com.booboot.vndbandroid.adapter.vndetails.VNExpandableListAdapter;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.DB;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.VNStatServer;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndb.Links;
import com.booboot.vndbandroid.bean.vndb.Options;
import com.booboot.vndbandroid.bean.vndbandroid.Priority;
import com.booboot.vndbandroid.bean.vndbandroid.Status;
import com.booboot.vndbandroid.bean.vndbandroid.VNlistItem;
import com.booboot.vndbandroid.bean.vndbandroid.Vote;
import com.booboot.vndbandroid.bean.vndbandroid.VotelistItem;
import com.booboot.vndbandroid.bean.vndbandroid.WishlistItem;
import com.booboot.vndbandroid.bean.vnstat.SimilarNovel;
import com.booboot.vndbandroid.factory.PlaceholderPictureFactory;
import com.booboot.vndbandroid.factory.VNDetailsFactory;
import com.booboot.vndbandroid.listener.VNDetailsListener;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;
import com.booboot.vndbandroid.util.Pixels;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class VNDetailsActivity extends AppCompatActivity {
    public int spoilerLevel = -1;
    private ActionBar actionBar;
    private Item vn;
    private VNlistItem vnlistVn;
    private WishlistItem wishlistVn;
    private VotelistItem votelistVn;

    private List<Item> characters;
    private LinkedHashMap<String, List<Item>> releases;
    private List<Item> releasesList;
    private List<SimilarNovel> similarNovels;

    private ImageButton image;
    private Button statusButton;
    private Button wishlistButton;
    private Button votesButton;
    private TextView notesTextView;
    private ImageButton notesEditButton;

    private VNDetailsListener listener;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    private VNDetailsElement charactersSubmenu;
    private VNDetailsElement releasesSubmenu;
    private VNDetailsElement languagesSubmenu;
    private VNDetailsElement platformsSubmenu;
    private VNDetailsElement animesSubmenu;
    private VNDetailsElement relationsSubmenu;
    private VNDetailsElement similarNovelsSubmenu;
    private VNDetailsElement tagsSubmenu;
    private VNDetailsElement genresSubmenu;
    private VNDetailsElement screensSubmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(VNTypeFragment.VN_ARG);

        if (savedInstanceState != null) {
            spoilerLevel = savedInstanceState.getInt("SPOILER_LEVEL");
        }

        if (spoilerLevel < 0) {
            if (SettingsManager.getSpoilerCompleted(this) && vn.getStatus() == Status.FINISHED)
                spoilerLevel = 2;
            else
                spoilerLevel = SettingsManager.getSpoilerLevel(this);
        }

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
        if (Cache.similarNovels.get(vn.getId()) != null) {
            similarNovels = Cache.similarNovels.get(vn.getId());
        }
        if (vn.getLanguages() == null && Cache.vns.get(vn.getId()) != null && Cache.vns.get(vn.getId()).getLanguages() != null) {
            vn.setLanguages(Cache.vns.get(vn.getId()).getLanguages());
        }
        if (vn.getPlatforms() == null && Cache.vns.get(vn.getId()) != null && Cache.vns.get(vn.getId()).getPlatforms() != null) {
            vn.setPlatforms(Cache.vns.get(vn.getId()).getPlatforms());
        }
        if (vn.getAnime() == null && Cache.vns.get(vn.getId()) != null && Cache.vns.get(vn.getId()).getAnime() != null) {
            vn.setAnime(Cache.vns.get(vn.getId()).getAnime());
        }
        if (vn.getRelations() == null && Cache.vns.get(vn.getId()) != null && Cache.vns.get(vn.getId()).getRelations() != null) {
            vn.setRelations(Cache.vns.get(vn.getId()).getRelations());
        }
        if (vn.getTags() == null && Cache.vns.get(vn.getId()) != null && Cache.vns.get(vn.getId()).getTags() != null) {
            vn.setTags(Cache.vns.get(vn.getId()).getTags());
        }
        if (vn.getScreens() == null && Cache.vns.get(vn.getId()) != null && Cache.vns.get(vn.getId()).getScreens() != null) {
            vn.setScreens(Cache.vns.get(vn.getId()).getScreens());
        }

        initExpandableListView();

        image = (ImageButton) findViewById(R.id.image);
        statusButton = (Button) findViewById(R.id.statusButton);
        wishlistButton = (Button) findViewById(R.id.wishlistButton);
        votesButton = (Button) findViewById(R.id.votesButton);
        notesTextView = (TextView) findViewById(R.id.notesTextView);
        notesTextView.setText(vnlistVn != null ? vnlistVn.getNotes() : "");
        listener = new VNDetailsListener(this, vn, notesTextView);

        notesEditButton = (ImageButton) findViewById(R.id.notesEditButton);
        notesEditButton.setColorFilter(Utils.getThemeColor(this, R.attr.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        notesEditButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        notesEditButton.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                        notesEditButton.setAlpha(0.4f);
                        break;

                    case MotionEvent.ACTION_UP:
                        notesEditButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        notesEditButton.setAlpha(1.0f);
                        AlertDialog.Builder builder = new AlertDialog.Builder(VNDetailsActivity.this);
                        builder.setTitle("Notes");
                        final LinearLayout params = new LinearLayout(VNDetailsActivity.this);
                        params.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        params.setPadding(Pixels.px(15, VNDetailsActivity.this), 0, Pixels.px(15, VNDetailsActivity.this), 0);
                        final EditText input = new EditText(VNDetailsActivity.this);
                        input.setSingleLine();
                        input.setText(notesTextView.getText());
                        input.setSelection(input.getText().length());
                        input.setMaxHeight(Pixels.px(200, VNDetailsActivity.this));
                        params.addView(input, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        builder.setView(params);
                        listener.setNotesInput(input);
                        listener.setPopupButton(statusButton);
                        builder.setPositiveButton("Save", listener);
                        builder.setNegativeButton("Cancel", listener);
                        builder.setNeutralButton("Clear", listener);

                        final AlertDialog dialog = builder.create();
                        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                }
                            }
                        });
                        dialog.show();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        notesEditButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        notesEditButton.setAlpha(1.0f);
                        break;
                }
                return true;
            }
        });

        actionBar = getSupportActionBar();
        actionBar.setTitle(vn.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        statusButton.setText(Status.toString(vnlistVn != null ? vnlistVn.getStatus() : -1));
        wishlistButton.setText(Priority.toString(wishlistVn != null ? wishlistVn.getPriority() : -1));
        votesButton.setText(Vote.toString(votelistVn != null ? votelistVn.getVote() : -1));
        toggleButtons();

        Utils.setButtonColor(this, statusButton);
        Utils.setButtonColor(this, wishlistButton);
        Utils.setButtonColor(this, votesButton);

        if (vn.isImage_nsfw() && !SettingsManager.getNSFW(this)) {
            image.setImageResource(R.drawable.ic_nsfw);
        } else {
            ImageLoader.getInstance().displayImage(PlaceholderPictureFactory.USE_PLACEHOLDER ? PlaceholderPictureFactory.getPlaceholderPicture() : vn.getImage(), image);
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

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View groupView, int groupPosition, long id) {
                String groupName = (String) parent.getExpandableListAdapter().getGroup(groupPosition);
                boolean handledAsynchronously = initSubmenu(groupView, groupPosition, groupName);
                boolean hasChildren = parent.getExpandableListAdapter().getChildrenCount(groupPosition) > 0;

                if (!handledAsynchronously && !hasChildren) {
                    Toast.makeText(VNDetailsActivity.this, "Nothing to show here...", Toast.LENGTH_SHORT).show();
                }
                return handledAsynchronously || !hasChildren;
            }
        });
    }

    /**
     * Loads and displays a submenu's content.
     * Pattern: check if the variables are already init, otherwise check the content in database, otherwise send an API query.
     *
     * @param groupView     submenu's group view (to display an loader icon)
     * @param groupPosition submenu's group position
     * @param groupName     submenu's group title to identify which content has to be fetched
     * @return true if the data is fetched asynchronously. The submenu will then be expanded in a callback.
     */
    private boolean initSubmenu(final View groupView, final int groupPosition, final String groupName) {
        boolean alreadyInit = true, hasChildren = false;
        switch (groupName) {
            case VNDetailsFactory.TITLE_CHARACTERS:
                alreadyInit = Cache.characters.get(vn.getId()) != null;
                break;
            case VNDetailsFactory.TITLE_RELEASES:
                alreadyInit = Cache.releases.get(vn.getId()) != null;
                break;
            case VNDetailsFactory.TITLE_SIMILAR_NOVELS:
                alreadyInit = Cache.similarNovels.get(vn.getId()) != null;
                break;
            case VNDetailsFactory.TITLE_LANGUAGES:
                alreadyInit = vn.getLanguages() != null;
                break;
            case VNDetailsFactory.TITLE_PLATFORMS:
                alreadyInit = vn.getPlatforms() != null;
                break;
            case VNDetailsFactory.TITLE_ANIME:
                alreadyInit = vn.getAnime() != null;
                break;
            case VNDetailsFactory.TITLE_RELATIONS:
                alreadyInit = vn.getRelations() != null;
                break;
            case VNDetailsFactory.TITLE_TAGS:
                alreadyInit = vn.getTags() != null;
                hasChildren = expandableListAdapter.getChildrenCount(groupPosition) > 0;
                if (alreadyInit && !hasChildren) VNDetailsFactory.setTagsSubmenu(this);
                break;
            case VNDetailsFactory.TITLE_GENRES:
                alreadyInit = vn.getTags() != null;
                hasChildren = expandableListAdapter.getChildrenCount(groupPosition) > 0;
                if (alreadyInit && !hasChildren) VNDetailsFactory.setGenresSubmenu(this);
                break;
            case VNDetailsFactory.TITLE_SCREENSHOTS:
                alreadyInit = vn.getScreens() != null;
                break;
        }

        if (!alreadyInit) {
            /* Variables not init yet: going to fetch data asynchronously */
            showGroupLoader(groupView);

            new Thread() {
                public void run() {
                    boolean alreadyInDatabase = false;
                    switch (groupName) {
                        case VNDetailsFactory.TITLE_CHARACTERS:
                            characters = DB.loadCharacters(VNDetailsActivity.this, vn.getId());
                            alreadyInDatabase = characters.size() > 0;
                            break;
                        case VNDetailsFactory.TITLE_RELEASES:
                            releasesList = DB.loadReleases(VNDetailsActivity.this, vn.getId());
                            alreadyInDatabase = releasesList.size() > 0;
                            break;
                        case VNDetailsFactory.TITLE_SIMILAR_NOVELS:
                            /*
                            similarNovels = DB.loadSimilarNovels(VNDetailsActivity.this, vn.getId());
                            alreadyInDatabase = similarNovels.size() > 0;
                            */
                            break;
                        case VNDetailsFactory.TITLE_LANGUAGES:
                            vn.setLanguages(DB.loadLanguages(VNDetailsActivity.this, vn.getId()));
                            alreadyInDatabase = true;
                            break;
                        case VNDetailsFactory.TITLE_PLATFORMS:
                            vn.setPlatforms(DB.loadPlatforms(VNDetailsActivity.this, vn.getId()));
                            alreadyInDatabase = true;
                            break;
                        case VNDetailsFactory.TITLE_ANIME:
                            vn.setAnime(DB.loadAnimes(VNDetailsActivity.this, vn.getId()));
                            alreadyInDatabase = true;
                            break;
                        case VNDetailsFactory.TITLE_RELATIONS:
                            vn.setRelations(DB.loadRelations(VNDetailsActivity.this, vn.getId()));
                            alreadyInDatabase = true;
                            break;
                        case VNDetailsFactory.TITLE_TAGS:
                        case VNDetailsFactory.TITLE_GENRES:
                            vn.setTags(DB.loadTags(VNDetailsActivity.this, vn.getId()));
                            alreadyInDatabase = true;
                            break;
                        case VNDetailsFactory.TITLE_SCREENSHOTS:
                            vn.setScreens(DB.loadScreens(VNDetailsActivity.this, vn.getId()));
                            alreadyInDatabase = true;
                            break;
                    }

                    if (alreadyInDatabase) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                switch (groupName) {
                                    case VNDetailsFactory.TITLE_CHARACTERS:
                                        Cache.characters.put(vn.getId(), characters);
                                        groupCharactersByRole();
                                        VNDetailsFactory.setCharactersSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_RELEASES:
                                        Cache.releases.put(vn.getId(), releasesList);
                                        groupReleasesByLanguage(releasesList);
                                        VNDetailsFactory.setReleasesSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_SIMILAR_NOVELS:
                                        Cache.similarNovels.put(vn.getId(), similarNovels);
                                        VNDetailsFactory.setSimilarNovelsSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_LANGUAGES:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setLanguagesSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_PLATFORMS:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setPlatformsSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_ANIME:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setAnimesSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_RELATIONS:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setRelationsSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_TAGS:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setTagsSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_GENRES:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setGenresSubmenu(VNDetailsActivity.this);
                                        break;
                                    case VNDetailsFactory.TITLE_SCREENSHOTS:
                                        Cache.vns.put(vn.getId(), vn);
                                        VNDetailsFactory.setScreensSubmenu(VNDetailsActivity.this);
                                        break;
                                }

                                hideGroupLoader(groupView, groupPosition);
                            }
                        });
                    } else {
                        /* Database tables not init yet: going to send the API query */
                        switch (groupName) {
                            case VNDetailsFactory.TITLE_CHARACTERS:
                                VNDBServer.get("character", Cache.CHARACTER_FLAGS, "(vn = " + vn.getId() + ")", Options.create(true, true, 0), 0, VNDetailsActivity.this, new Callback() {
                                    @Override
                                    protected void config() {
                                        if (results.getItems().isEmpty()) {
                                            Cache.characters.put(vn.getId(), new ArrayList<Item>());
                                            characters = Cache.characters.get(vn.getId());
                                        } else {
                                            characters = results.getItems();
                                            Cache.characters.put(vn.getId(), characters);
                                            DB.saveCharacters(VNDetailsActivity.this, characters, vn.getId());
                                        }

                                        if (characters == null) {
                                            hideGroupLoader(groupView, groupPosition);
                                            return;
                                        }

                                        groupCharactersByRole();
                                        VNDetailsFactory.setCharactersSubmenu(VNDetailsActivity.this);
                                        hideGroupLoader(groupView, groupPosition);
                                    }
                                }, getSubmenuCallback(groupView, groupPosition));
                                break;

                            case VNDetailsFactory.TITLE_RELEASES:
                                VNDBServer.get("release", Cache.RELEASE_FLAGS, "(vn = " + vn.getId() + ")", Options.create(1, 25, "released", false, true, true, 0), 1, VNDetailsActivity.this, new Callback() {
                                    @Override
                                    protected void config() {
                                        List<Item> releasesList;
                                        if (results.getItems().isEmpty()) {
                                            Cache.releases.put(vn.getId(), new ArrayList<Item>());
                                            releasesList = Cache.releases.get(vn.getId());
                                        } else {
                                            releasesList = results.getItems();
                                            Cache.releases.put(vn.getId(), releasesList);
                                            DB.saveReleases(VNDetailsActivity.this, releasesList, vn.getId());
                                        }

                                        if (releasesList == null) {
                                            hideGroupLoader(groupView, groupPosition);
                                            return;
                                        }

                                        groupReleasesByLanguage(releasesList);
                                        VNDetailsFactory.setReleasesSubmenu(VNDetailsActivity.this);
                                        hideGroupLoader(groupView, groupPosition);
                                    }
                                }, getSubmenuCallback(groupView, groupPosition));
                                break;

                            case VNDetailsFactory.TITLE_SIMILAR_NOVELS:
                                VNStatServer.get("similar", vn.getId(), new Callback() {
                                    @Override
                                    protected void config() {
                                        if (vnStatResults.getSimilar().isEmpty()) {
                                            Cache.similarNovels.put(vn.getId(), new ArrayList<SimilarNovel>());
                                            similarNovels = Cache.similarNovels.get(vn.getId());
                                        } else {
                                            similarNovels = vnStatResults.getSimilar();
                                            Cache.similarNovels.put(vn.getId(), similarNovels);

                                            // DB.saveSimilarNovels(VNDetailsActivity.this, similarNovels, vn.getId());
                                        }

                                        if (similarNovels == null) {
                                            hideGroupLoader(groupView, groupPosition);
                                            return;
                                        }

                                        // groupCharactersByRole();
                                        VNDetailsFactory.setSimilarNovelsSubmenu(VNDetailsActivity.this);
                                        hideGroupLoader(groupView, groupPosition);
                                    }
                                }, getSubmenuCallback(groupView, groupPosition));
                                break;
                        }

                    }
                }
            }.start();
            return true;
        }
        return false;
    }

    private Callback getSubmenuCallback(final View groupView, final int groupPosition) {
        return new Callback() {
            @Override
            public void config() {
                showToast(VNDetailsActivity.this, message);
                hideGroupLoader(groupView, groupPosition);
            }
        };
    }

    private void hideGroupLoader(final View groupView, final int groupPosition) {
        expandableListView.expandGroup(groupPosition, true);
        ImageView groupLoader = (ImageView) groupView.findViewById(R.id.groupLoader);
        groupLoader.clearAnimation();
        groupLoader.setVisibility(View.INVISIBLE);
        groupView.setEnabled(true);

        boolean hasChildren = expandableListAdapter.getChildrenCount(groupPosition) > 0;
        if (!hasChildren) {
            Toast.makeText(VNDetailsActivity.this, "Nothing to show here...", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGroupLoader(View groupView) {
        groupView.setEnabled(false);
        ImageView groupLoader = (ImageView) groupView.findViewById(R.id.groupLoader);
        Utils.tintImage(this, groupLoader, R.attr.colorPrimaryDark, true);
        groupLoader.startAnimation(AnimationUtils.loadAnimation(VNDetailsActivity.this, R.anim.infinite_rotation));
        groupLoader.setVisibility(View.VISIBLE);
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

    /**
     * Sorting the characters with their role (Protagonist then main characters etc.)
     */
    private void groupCharactersByRole() {
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
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                break;

            case R.id.action_view_on_vndb:
                Utils.openURL(this, Links.VNDB_PAGE + vn.getId());
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

    public void toggleButtons() {
        vnlistVn = Cache.vnlist.get(vn.getId());
        wishlistVn = Cache.wishlist.get(vn.getId());
        votelistVn = Cache.votelist.get(vn.getId());

        boolean hideWish = votelistVn != null && wishlistVn == null;
        boolean hideVote = votelistVn == null && wishlistVn != null;
        wishlistButton.setVisibility(hideWish ? View.GONE : View.VISIBLE);
        votesButton.setVisibility(hideVote ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("SPOILER_LEVEL", spoilerLevel);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }

    @Override
    protected void onDestroy() {
        DB.saveVnlist(this);
        DB.saveVotelist(this);
        DB.saveWishlist(this);
        DB.saveVNs(this);
        super.onDestroy();
    }

    public void setCharactersSubmenu(VNDetailsElement characterElement) {
        this.charactersSubmenu = characterElement;
    }

    public VNDetailsElement getReleasesSubmenu() {
        return releasesSubmenu;
    }

    public void setReleasesSubmenu(VNDetailsElement releasesSubmenu) {
        this.releasesSubmenu = releasesSubmenu;
    }

    public VNDetailsElement getCharactersSubmenu() {
        return charactersSubmenu;
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

    public void setLanguagesSubmenu(VNDetailsElement languagesSubmenu) {
        this.languagesSubmenu = languagesSubmenu;
    }

    public VNDetailsElement getLanguagesSubmenu() {
        return languagesSubmenu;
    }

    public VNDetailsElement getPlatformsSubmenu() {
        return platformsSubmenu;
    }

    public void setPlatformsSubmenu(VNDetailsElement platformsSubmenu) {
        this.platformsSubmenu = platformsSubmenu;
    }

    public VNDetailsElement getAnimesSubmenu() {
        return animesSubmenu;
    }

    public void setAnimesSubmenu(VNDetailsElement animesSubmenu) {
        this.animesSubmenu = animesSubmenu;
    }

    public VNDetailsElement getRelationsSubmenu() {
        return relationsSubmenu;
    }

    public void setRelationsSubmenu(VNDetailsElement relationsSubmenu) {
        this.relationsSubmenu = relationsSubmenu;
    }

    public VNDetailsElement getTagsSubmenu() {
        return tagsSubmenu;
    }

    public void setTagsSubmenu(VNDetailsElement tagsSubmenu) {
        this.tagsSubmenu = tagsSubmenu;
    }

    public VNDetailsElement getScreensSubmenu() {
        return screensSubmenu;
    }

    public void setScreensSubmenu(VNDetailsElement screensSubmenu) {
        this.screensSubmenu = screensSubmenu;
    }

    public VNDetailsElement getGenresSubmenu() {
        return genresSubmenu;
    }

    public void setGenresSubmenu(VNDetailsElement genresSubmenu) {
        this.genresSubmenu = genresSubmenu;
    }

    public VNDetailsElement getSimilarNovelsSubmenu() {
        return similarNovelsSubmenu;
    }

    public void setSimilarNovelsSubmenu(VNDetailsElement similarNovelsSubmenu) {
        this.similarNovelsSubmenu = similarNovelsSubmenu;
    }

    public List<SimilarNovel> getSimilarNovels() {
        return similarNovels;
    }

    public void setSimilarNovels(List<SimilarNovel> similarNovels) {
        this.similarNovels = similarNovels;
    }
}
