package com.booboot.vndbandroid.adapter.vndetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.MainActivity;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.bean.vndb.Fields;
import com.booboot.vndbandroid.bean.vndb.Item;
import com.booboot.vndbandroid.bean.vndbandroid.Priority;
import com.booboot.vndbandroid.bean.vndbandroid.Status;
import com.booboot.vndbandroid.bean.vndbandroid.VNlistItem;
import com.booboot.vndbandroid.bean.vndbandroid.Vote;
import com.booboot.vndbandroid.bean.vndbandroid.VotelistItem;
import com.booboot.vndbandroid.bean.vndbandroid.WishlistItem;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.Utils;

import java.util.Date;

/**
 * Created by od on 12/04/2016.
 */
public class VNDetailsListener implements PopupMenu.OnMenuItemClickListener, DialogInterface.OnClickListener, View.OnClickListener {
    private VNDetailsActivity activity;
    private Item vn;
    private Button popupButton;
    private TextView notesTextView;
    private EditText notesInput;

    public VNDetailsListener(VNDetailsActivity activity, Item vn, TextView notesTextView) {
        this.activity = activity;
        this.vn = vn;
        this.notesTextView = notesTextView;
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        if (!ConnectionReceiver.isConnected()) {
            Callback.showToast(activity, ConnectionReceiver.CONNECTION_ERROR_MESSAGE);
            return true;
        }

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

            case R.id.item_other_vote:
                type = "votelist";
                buildOtherVoteDialog(type, fields, item);
                return true;

            case R.id.item_no_vote:
                type = "votelist";
                fields = null;
                break;

            default:
                return false;
        }

        sendSetRequest(type, fields, item);
        return true;
    }

    private void sendSetRequest(String type, final Fields fields, final MenuItem item) {
        VNDBServer.set(type, vn.getId(), fields, activity, new Callback() {
            @Override
            /**
             * It is necessary to make another switch here to update our cache, because if there is an error
             * during the request, our cache's state would be different from the actual account state and there would be discrepancies.
             */
            protected void config() {
                if (popupButton != null)
                    popupButton.setText(item.getTitle());

                VNlistItem vnlistItem;
                WishlistItem wishlistItem;
                VotelistItem votelistItem;

                switch (item.getItemId()) {
                    case R.id.item_playing:
                        vnlistItem = Cache.vnlist.get(vn.getId());
                        if (vnlistItem == null) {
                            vnlistItem = new VNlistItem();
                            vnlistItem.setAdded((int) new Date().getTime());
                            vnlistItem.setVn(vn.getId());
                        }
                        vnlistItem.setStatus(Status.PLAYING);
                        Cache.vnlist.put(vn.getId(), vnlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_finished:
                        vnlistItem = Cache.vnlist.get(vn.getId());
                        if (vnlistItem == null) {
                            vnlistItem = new VNlistItem();
                            vnlistItem.setAdded((int) new Date().getTime());
                            vnlistItem.setVn(vn.getId());
                        }
                        vnlistItem.setStatus(Status.FINISHED);
                        Cache.vnlist.put(vn.getId(), vnlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_stalled:
                        vnlistItem = Cache.vnlist.get(vn.getId());
                        if (vnlistItem == null) {
                            vnlistItem = new VNlistItem();
                            vnlistItem.setAdded((int) new Date().getTime());
                            vnlistItem.setVn(vn.getId());
                        }
                        vnlistItem.setStatus(Status.STALLED);
                        Cache.vnlist.put(vn.getId(), vnlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_dropped:
                        vnlistItem = Cache.vnlist.get(vn.getId());
                        if (vnlistItem == null) {
                            vnlistItem = new VNlistItem();
                            vnlistItem.setAdded((int) new Date().getTime());
                            vnlistItem.setVn(vn.getId());
                        }
                        vnlistItem.setStatus(Status.DROPPED);
                        Cache.vnlist.put(vn.getId(), vnlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_unknown:
                        vnlistItem = Cache.vnlist.get(vn.getId());
                        if (vnlistItem == null) {
                            vnlistItem = new VNlistItem();
                            vnlistItem.setAdded((int) new Date().getTime());
                            vnlistItem.setVn(vn.getId());
                        }
                        vnlistItem.setStatus(Status.UNKNOWN);
                        Cache.vnlist.put(vn.getId(), vnlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_no_status:
                        Cache.vnlist.remove(vn.getId());
                        popupButton.setText(Status.DEFAULT);
                        notesTextView.setText("");
                        break;

                    case R.id.item_high:
                        wishlistItem = Cache.wishlist.get(vn.getId());
                        if (wishlistItem == null) {
                            wishlistItem = new WishlistItem();
                            wishlistItem.setAdded((int) new Date().getTime());
                            wishlistItem.setVn(vn.getId());
                        }
                        wishlistItem.setPriority(Priority.HIGH);
                        Cache.wishlist.put(vn.getId(), wishlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_medium:
                        wishlistItem = Cache.wishlist.get(vn.getId());
                        if (wishlistItem == null) {
                            wishlistItem = new WishlistItem();
                            wishlistItem.setAdded((int) new Date().getTime());
                            wishlistItem.setVn(vn.getId());
                        }
                        wishlistItem.setPriority(Priority.MEDIUM);
                        Cache.wishlist.put(vn.getId(), wishlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_low:
                        wishlistItem = Cache.wishlist.get(vn.getId());
                        if (wishlistItem == null) {
                            wishlistItem = new WishlistItem();
                            wishlistItem.setAdded((int) new Date().getTime());
                            wishlistItem.setVn(vn.getId());
                        }
                        wishlistItem.setPriority(Priority.LOW);
                        Cache.wishlist.put(vn.getId(), wishlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_blacklist:
                        wishlistItem = Cache.wishlist.get(vn.getId());
                        if (wishlistItem == null) {
                            wishlistItem = new WishlistItem();
                            wishlistItem.setAdded((int) new Date().getTime());
                            wishlistItem.setVn(vn.getId());
                        }
                        wishlistItem.setPriority(Priority.BLACKLIST);
                        Cache.wishlist.put(vn.getId(), wishlistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        break;

                    case R.id.item_no_wishlist:
                        Cache.wishlist.remove(vn.getId());
                        popupButton.setText(Priority.DEFAULT);
                        break;

                    case R.id.item_10:
                    case R.id.item_9:
                    case R.id.item_8:
                    case R.id.item_7:
                    case R.id.item_6:
                    case R.id.item_5:
                    case R.id.item_4:
                    case R.id.item_3:
                    case R.id.item_2:
                    case R.id.item_1:
                    case R.id.item_other_vote:
                        votelistItem = Cache.votelist.get(vn.getId());
                        if (votelistItem == null) {
                            votelistItem = new VotelistItem();
                            votelistItem.setAdded((int) new Date().getTime());
                            votelistItem.setVn(vn.getId());
                        }
                        votelistItem.setVote(fields.getVote());
                        Cache.votelist.put(vn.getId(), votelistItem);
                        if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                        popupButton.setText(Vote.toString(fields.getVote()));
                        break;

                    case R.id.item_no_vote:
                        Cache.votelist.remove(vn.getId());
                        popupButton.setText(Vote.DEFAULT);
                        break;

                    default:
                        return;
                }

                activity.toggleButtons();
                if (MainActivity.instance != null)
                    MainActivity.instance.refreshVnlistFragment();
            }
        }, Callback.errorCallback(activity));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_spoil_0:
                activity.spoilerLevel = 0;
                break;

            case R.id.item_spoil_1:
                activity.spoilerLevel = 1;
                break;

            case R.id.item_spoil_2:
                activity.spoilerLevel = 2;
                break;

            case R.id.check_nsfw:
                activity.nsfwLevel = ((CheckBox) view).isChecked() ? 1 : 0;
                break;
        }

        Utils.recreate(activity);
    }

    private void buildOtherVoteDialog(final String type, final Fields fields, final MenuItem item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Custom vote");
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.other_vote_dialog, null);
        builder.setView(dialogView);
        final EditText otherVoteInput = (EditText) dialogView.findViewById(R.id.otherVoteInput);

        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String vote = otherVoteInput.getText().toString().trim();
                        if (Vote.isValid(vote)) {
                            fields.setVote(Math.round(Float.valueOf(vote) * 10));
                            sendSetRequest(type, fields, item);
                            dialog.cancel();
                        } else {
                            otherVoteInput.setError("Invalid vote.");
                        }
                    }
                });
            }
        });
        otherVoteInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        dialog.show();
    }

    private void sendNotesRequest(String type, final Fields fields) {
        VNDBServer.set(type, vn.getId(), fields, activity, new Callback() {
            @Override
            protected void config() {
                VNlistItem vnlistItem = Cache.vnlist.get(vn.getId());
                if (vnlistItem == null) {
                    vnlistItem = new VNlistItem();
                    vnlistItem.setVn(vn.getId());
                    vnlistItem.setStatus(Status.UNKNOWN);
                    vnlistItem.setAdded((int) new Date().getTime());
                    if (popupButton != null)
                        popupButton.setText(Status.toString(Status.UNKNOWN));
                }
                vnlistItem.setNotes(fields.getNotes());
                notesTextView.setText(fields.getNotes());
                Cache.vnlist.put(vn.getId(), vnlistItem);
                if (Cache.vns.get(vn.getId()) == null) Cache.vns.put(vn.getId(), vn);
                if (MainActivity.instance != null)
                    MainActivity.instance.refreshVnlistFragment();
            }
        }, Callback.errorCallback(activity));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (notesInput == null) Callback.showToast(activity, "There are no notes to save.");
        Fields fields = new Fields();
        VNlistItem vnlistItem;
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                vnlistItem = Cache.vnlist.get(vn.getId());
                fields.setStatus(vnlistItem == null ? Status.UNKNOWN : vnlistItem.getStatus());
                fields.setNotes(notesInput.getText().toString());
                sendNotesRequest("vnlist", fields);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                dialog.cancel();
                vnlistItem = Cache.vnlist.get(vn.getId());
                fields.setStatus(vnlistItem == null ? Status.UNKNOWN : vnlistItem.getStatus());
                fields.setNotes("");
                sendNotesRequest("vnlist", fields);
                break;

        }
    }

    public void setPopupButton(Button popupButton) {
        this.popupButton = popupButton;
    }

    public void setNotesInput(EditText notesInput) {
        this.notesInput = notesInput;
    }
}
