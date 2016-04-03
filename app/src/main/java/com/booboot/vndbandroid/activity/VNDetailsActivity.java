package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.CustomExpandableListAdapter;
import com.booboot.vndbandroid.adapter.VNDetailsElement;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Category;
import com.booboot.vndbandroid.api.bean.Fields;
import com.booboot.vndbandroid.api.bean.Genre;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Language;
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.api.bean.Platform;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Screen;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Tag;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.db.DB;
import com.booboot.vndbandroid.util.Bitmap;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Lightbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class VNDetailsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public final static String TITLE_INFORMATION = "Information";
    public final static String TITLE_DESCRIPTION = "Description";
    public final static String TITLE_GENRES = "Genres";
    public final static String TITLE_SCREENSHOTS = "Screenshots";
    public final static String TITLE_STATS = "Stats";
    public final static String TITLE_TAGS = "Tags";
    public final static String TITLE_PLATFORMS = "Platforms";
    public final static String TITLE_LANGUAGES = "Languages";

    private ActionBar actionBar;
    private Item vn;
    private Item vnlistVn;
    private Item wishlistVn;
    private Item votelistVn;

    private ImageButton image;
    private Button statusButton;
    private Button wishlistButton;
    private Button votesButton;
    private Button popupButton;

    private boolean shouldRecreateActivity = false;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(VNTypeFragment.VN_ARG);
        vnlistVn = DB.vnlist.get(vn.getId());
        wishlistVn = DB.wishlist.get(vn.getId());
        votelistVn = DB.votelist.get(vn.getId());

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

        new Thread() {
            public void run() {
                image.setImageDrawable(Bitmap.drawableFromUrl(vn.getImage()));
            }
        }.start();

        Lightbox.set(this, image, vn.getImage());
    }

    private void initExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = getExpandableListData();
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.addHeaderView(getLayoutInflater().inflate(R.layout.vn_details_header, null));
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        popupButton.setText(item.getTitle());
        String type;
        Fields fields = new Fields();

        switch (item.getItemId()) {
            case R.id.item_playing:
                type = "vnlist";
                fields.setStatus(Status.PLAYING);
                break;
            case R.id.item_finished:
                type = "vnlist";
                fields.setStatus(Status.FINISHED);
                break;
            case R.id.item_stalled:
                type = "vnlist";
                fields.setStatus(Status.STALLED);
                break;
            case R.id.item_dropped:
                type = "vnlist";
                fields.setStatus(Status.DROPPED);
                break;
            case R.id.item_unknown:
                type = "vnlist";
                fields.setStatus(Status.UNKNOWN);
                break;
            case R.id.item_no_status:
                type = "vnlist";
                fields = null;
                break;
            case R.id.item_high:
                type = "wishlist";
                fields.setPriority(Priority.HIGH);
                break;
            case R.id.item_medium:
                type = "wishlist";
                fields.setPriority(Priority.MEDIUM);
                break;
            case R.id.item_low:
                type = "wishlist";
                fields.setPriority(Priority.LOW);
                break;
            case R.id.item_blacklist:
                type = "wishlist";
                fields.setPriority(Priority.BLACKLIST);
                break;
            case R.id.item_no_wishlist:
                type = "wishlist";
                fields = null;
                break;
            case R.id.item_10:
                type = "votelist";
                fields.setVote(100);
                break;
            case R.id.item_9:
                type = "votelist";
                fields.setVote(90);
                break;
            case R.id.item_8:
                type = "votelist";
                fields.setVote(80);
                break;
            case R.id.item_7:
                type = "votelist";
                fields.setVote(70);
                break;
            case R.id.item_6:
                type = "votelist";
                fields.setVote(60);
                break;
            case R.id.item_5:
                type = "votelist";
                fields.setVote(50);
                break;
            case R.id.item_4:
                type = "votelist";
                fields.setVote(40);
                break;
            case R.id.item_3:
                type = "votelist";
                fields.setVote(30);
                break;
            case R.id.item_2:
                type = "votelist";
                fields.setVote(20);
                break;
            case R.id.item_1:
                type = "votelist";
                fields.setVote(10);
                break;
            case R.id.item_no_vote:
                type = "votelist";
                fields = null;
                break;
            default:
                return false;
        }

        VNDBServer.set(type, vn.getId(), fields, this, null, Callback.errorCallback(this));
        shouldRecreateActivity = true;
        return true;
    }

    public void showStatusPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.status, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popupButton = statusButton;
        popup.show();
    }

    public void showWishlistPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.wishlist, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popupButton = wishlistButton;
        popup.show();
    }

    public void showVotesPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.votes, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popupButton = votesButton;
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (shouldRecreateActivity) {
                    DB.loadData(getApplicationContext(), new Callback() {
                        @Override
                        protected void config() {
                            VNDetailsActivity.this.finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });
                } else {
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public LinkedHashMap<String, VNDetailsElement> getExpandableListData() {
        LinkedHashMap<String, VNDetailsElement> expandableListDetail = new LinkedHashMap<>();

        List<String> infoLeft = new ArrayList<>();
        List<String> infoRight = new ArrayList<>();
        List<Integer> infoRightImages = new ArrayList<>();
        infoLeft.add("Title");
        infoRight.add(vn.getTitle());
        infoRightImages.add(-1);
        if (vn.getOriginal() != null) {
            infoLeft.add("Original title");
            infoRight.add(vn.getOriginal());
            infoRightImages.add(-1);
        }

        try {
            infoLeft.add("Released date");
            Date released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(vn.getReleased());
            infoRight.add(new SimpleDateFormat("d MMMM yyyy", Locale.US).format(released));
            infoRightImages.add(-1);
        } catch (ParseException e) {
        }

        if (vn.getAliases() != null) {
            infoLeft.add("Aliases");
            infoRight.add(vn.getAliases().replace("\n", "<br>"));
            infoRightImages.add(-1);
        }

        infoLeft.add("Length");
        infoRight.add(vn.getLengthString());
        infoRightImages.add(vn.getLengthImage());

        infoLeft.add("Links");
        Links links = vn.getLinks();
        String htmlLinks = "";
        if (links.getWikipedia() != null) htmlLinks += "<a href=\"" + Links.WIKIPEDIA + links.getWikipedia() + "\">Wikipedia</a>";
        if (links.getEncubed() != null) htmlLinks += "<br><a href=\"" + Links.ENCUBED + links.getEncubed() + "\">Encubed</a>";
        if (links.getRenai() != null) htmlLinks += "<br><a href=\"" + Links.RENAI + links.getRenai() + "\">Renai</a>";
        infoRight.add(htmlLinks);
        infoRightImages.add(-1);

        List<String> description = new ArrayList<>();
        description.add(vn.getDescription());

        List<String> genres = new ArrayList<>();
        for (List<Number> tag : vn.getTags()) {
            String tagName = Tag.getTags(this).get(tag.get(0).intValue()).getName();
            if (Genre.contains(tagName)) {
                genres.add(tagName);
            }
        }

        List<String> tags = new ArrayList<>();
        List<Integer> tags_images = new ArrayList<>();
        List<String> alreadyProcessedTags = new ArrayList<>();
        for (List<Number> cat : vn.getTags()) {
            String currentCategory = Tag.getTags(this).get(cat.get(0).intValue()).getCat();
            if (!alreadyProcessedTags.contains(currentCategory)) {
                alreadyProcessedTags.add(currentCategory);
                tags.add("<b>" + Category.CATEGORIES.get(currentCategory) + " :</b>");
                tags_images.add(-1);
                for (List<Number> tag : vn.getTags()) {
                    String tagCat = Tag.getTags(this).get(tag.get(0).intValue()).getCat();
                    if (tagCat.equals(currentCategory)) {
                        String tagName = Tag.getTags(this).get(tag.get(0).intValue()).getName();
                        tags.add(tagName);
                        tags_images.add(Tag.getScoreImage(tag));
                    }
                }
            }
        }

        List<String> screenshots = new ArrayList<>();
        for (Screen screenshot : vn.getScreens()) {
            screenshots.add(screenshot.getImage());
        }

        List<String> statLeft = new ArrayList<>();
        List<String> statRight = new ArrayList<>();
        List<Integer> statRightImages = new ArrayList<>();
        statLeft.add("Popularity");
        statRight.add(vn.getPopularity() + "%");
        statRightImages.add(vn.getPopularityImage());
        statLeft.add("Rating");
        statRight.add(vn.getRating() + " (" + Vote.getName(vn.getRating()) + ")<br>" + vn.getVotecount() + " votes total");
        statRightImages.add(vn.getRatingImage());

        List<String> platforms = new ArrayList<>();
        List<Integer> platforms_images = new ArrayList<>();
        for (String platform : vn.getPlatforms()) {
            platforms.add(Platform.FULL_TEXT.get(platform));
            platforms_images.add(Platform.IMAGES.get(platform));
        }

        List<String> languages = new ArrayList<>();
        List<Integer> languages_flags = new ArrayList<>();
        for (String language : vn.getLanguages()) {
            languages.add(Language.FULL_TEXT.get(language));
            languages_flags.add(Language.FLAGS.get(language));
        }

        expandableListDetail.put(TITLE_INFORMATION, new VNDetailsElement(null, infoLeft, infoRight, infoRightImages, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_DESCRIPTION, new VNDetailsElement(null, description, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_GENRES, new VNDetailsElement(null, genres, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_SCREENSHOTS, new VNDetailsElement(null, screenshots, null, null, VNDetailsElement.TYPE_IMAGES));
        expandableListDetail.put(TITLE_STATS, new VNDetailsElement(null, statLeft, statRight, statRightImages, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_TAGS, new VNDetailsElement(tags_images, tags, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_PLATFORMS, new VNDetailsElement(platforms_images, platforms, null, null, VNDetailsElement.TYPE_TEXT));
        expandableListDetail.put(TITLE_LANGUAGES, new VNDetailsElement(languages_flags, languages, null, null, VNDetailsElement.TYPE_TEXT));

        return expandableListDetail;
    }
}
