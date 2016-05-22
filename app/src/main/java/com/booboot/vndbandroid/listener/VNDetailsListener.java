package com.booboot.vndbandroid.listener;

import android.provider.Settings;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.MainActivity;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Fields;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 12/04/2016.
 */
public class VNDetailsListener implements PopupMenu.OnMenuItemClickListener {
    private VNDetailsActivity activity;
    private Item vn;
    private Button popupButton;

    public VNDetailsListener(VNDetailsActivity activity, Item vn) {
        this.activity = activity;
        this.vn = vn;
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        if (!ConnectionReceiver.isConnected(activity)) {
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
                        vn.setVote(100);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_9:
                        vn.setVote(90);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_8:
                        vn.setVote(80);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_7:
                        vn.setVote(70);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_6:
                        vn.setVote(60);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_5:
                        vn.setVote(50);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_4:
                        vn.setVote(40);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_3:
                        vn.setVote(30);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_2:
                        vn.setVote(20);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_1:
                        vn.setVote(10);
                        Cache.votelist.put(vn.getId(), vn);
                        break;

                    case R.id.item_no_vote:
                        Cache.votelist.remove(vn.getId());
                        popupButton.setText(Vote.DEFAULT);
                        break;

                    default: return;
                }

                activity.toggleButtons();
                MainActivity.instance.getVnlistFragment().refresh();
            }
        }, Callback.errorCallback(activity));

        return true;
    }

    public void setPopupButton(Button popupButton) {
        this.popupButton = popupButton;
    }
}
