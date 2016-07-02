package com.booboot.vndbandroid.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.MainActivity;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Fields;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 12/04/2016.
 */
public class VNDetailsListener implements PopupMenu.OnMenuItemClickListener, DialogInterface.OnClickListener {
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
            Toast.makeText(activity, ConnectionReceiver.CONNECTION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
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
                //fields = null;
                fields.setStatus(0);
                fields.setNotes(vn.getNotes());
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

            case R.id.item_spoil_0:
                activity.spoilerLevel = 0;
                Utils.recreate(activity);
                return true;

            case R.id.item_spoil_1:
                activity.spoilerLevel = 1;
                Utils.recreate(activity);
                return true;

            case R.id.item_spoil_2:
                activity.spoilerLevel = 2;
                Utils.recreate(activity);
                return true;

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

                switch (item.getItemId()) {
                    case R.id.item_playing:
                        vn.setStatus(Status.PLAYING);
                        Cache.vnlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_finished:
                        vn.setStatus(Status.FINISHED);
                        Cache.vnlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_stalled:
                        vn.setStatus(Status.STALLED);
                        Cache.vnlist.put(vn.getId(), vn);

                        break;
                    case R.id.item_dropped:
                        vn.setStatus(Status.DROPPED);
                        Cache.vnlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_unknown:
                        vn.setStatus(Status.UNKNOWN);
                        Cache.vnlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_no_status:
                        Cache.vnlist.remove(vn.getId());
                        popupButton.setText(Status.DEFAULT);
                        break;

                    case R.id.item_high:
                        vn.setPriority(Priority.HIGH);
                        Cache.wishlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_medium:
                        vn.setPriority(Priority.MEDIUM);
                        Cache.wishlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_low:
                        vn.setPriority(Priority.LOW);
                        Cache.wishlist.put(vn.getId(), vn);
                        break;

                    case R.id.item_blacklist:
                        vn.setPriority(Priority.BLACKLIST);
                        Cache.wishlist.put(vn.getId(), vn);
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
                        vn.setVote(fields.getVote());
                        Cache.votelist.put(vn.getId(), vn);
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
                vn.setNotes(fields.getNotes());
                notesTextView.setText(fields.getNotes());
                Cache.vnlist.put(vn.getId(), vn);
                if (MainActivity.instance != null)
                    MainActivity.instance.refreshVnlistFragment();
            }
        }, Callback.errorCallback(activity));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (notesInput == null) Callback.showToast(activity, "There are no notes to save.");
        Fields fields = new Fields();
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                fields.setStatus(vn.getStatus());
                fields.setNotes(notesInput.getText().toString());
                sendNotesRequest("vnlist", fields);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                dialog.cancel();
                fields.setStatus(vn.getStatus());
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
