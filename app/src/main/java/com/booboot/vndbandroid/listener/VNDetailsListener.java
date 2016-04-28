package com.booboot.vndbandroid.listener;

import android.support.v7.widget.PopupMenu;
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
import com.booboot.vndbandroid.db.Cache;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.ConnectionReceiver;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 12/04/2016.
 */
public class VNDetailsListener implements PopupMenu.OnMenuItemClickListener {
    private VNDetailsActivity context;
    private Item vn;
    private Button popupButton;

    public VNDetailsListener(VNDetailsActivity context, Item vn) {
        this.context = context;
        this.vn = vn;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (!ConnectionReceiver.isConnected(context)) {
            Toast.makeText(context, ConnectionReceiver.CONNECTION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            return true;
        }

        if (popupButton != null)
            popupButton.setText(item.getTitle());
        String type;
        Fields fields = new Fields();

        switch (item.getItemId()) {
            case R.id.item_playing:
                type = "vnlist";
                fields.setStatus(Status.PLAYING);
                vn.setStatus(Status.PLAYING);
                Cache.vnlist.put(vn.getId(), vn);
                break;

            case R.id.item_finished:
                type = "vnlist";
                fields.setStatus(Status.FINISHED);
                vn.setStatus(Status.FINISHED);
                Cache.vnlist.put(vn.getId(), vn);
                break;

            case R.id.item_stalled:
                type = "vnlist";
                fields.setStatus(Status.STALLED);
                vn.setStatus(Status.STALLED);
                Cache.vnlist.put(vn.getId(), vn);

                break;
            case R.id.item_dropped:
                type = "vnlist";
                fields.setStatus(Status.DROPPED);
                vn.setStatus(Status.DROPPED);
                Cache.vnlist.put(vn.getId(), vn);
                break;

            case R.id.item_unknown:
                type = "vnlist";
                fields.setStatus(Status.UNKNOWN);
                vn.setStatus(Status.UNKNOWN);
                Cache.vnlist.put(vn.getId(), vn);
                break;

            case R.id.item_no_status:
                type = "vnlist";
                fields = null;
                Cache.vnlist.remove(vn.getId());
                break;

            case R.id.item_high:
                type = "wishlist";
                fields.setPriority(Priority.HIGH);
                vn.setPriority(Priority.HIGH);
                Cache.wishlist.put(vn.getId(), vn);
                break;

            case R.id.item_medium:
                type = "wishlist";
                fields.setPriority(Priority.MEDIUM);
                vn.setPriority(Priority.MEDIUM);
                Cache.wishlist.put(vn.getId(), vn);
                break;

            case R.id.item_low:
                type = "wishlist";
                fields.setPriority(Priority.LOW);
                vn.setPriority(Priority.LOW);
                Cache.wishlist.put(vn.getId(), vn);
                break;

            case R.id.item_blacklist:
                type = "wishlist";
                fields.setPriority(Priority.BLACKLIST);
                vn.setPriority(Priority.BLACKLIST);
                Cache.wishlist.put(vn.getId(), vn);
                break;

            case R.id.item_no_wishlist:
                type = "wishlist";
                fields = null;
                Cache.wishlist.remove(vn.getId());
                break;

            case R.id.item_10:
                type = "votelist";
                fields.setVote(100);
                vn.setVote(100);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_9:
                type = "votelist";
                fields.setVote(90);
                vn.setVote(90);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_8:
                type = "votelist";
                fields.setVote(80);
                vn.setVote(80);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_7:
                type = "votelist";
                fields.setVote(70);
                vn.setVote(70);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_6:
                type = "votelist";
                fields.setVote(60);
                vn.setVote(60);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_5:
                type = "votelist";
                fields.setVote(50);
                vn.setVote(50);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_4:
                type = "votelist";
                fields.setVote(40);
                vn.setVote(40);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_3:
                type = "votelist";
                fields.setVote(30);
                vn.setVote(30);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_2:
                type = "votelist";
                fields.setVote(20);
                vn.setVote(20);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_1:
                type = "votelist";
                fields.setVote(10);
                vn.setVote(10);
                Cache.votelist.put(vn.getId(), vn);
                break;

            case R.id.item_no_vote:
                type = "votelist";
                fields = null;
                Cache.votelist.remove(vn.getId());
                break;

            case R.id.item_spoil_0:
                VNDetailsActivity.spoilerLevel = 0;
                Utils.recreate(context);
                return true;

            case R.id.item_spoil_1:
                VNDetailsActivity.spoilerLevel = 1;
                Utils.recreate(context);
                return true;

            case R.id.item_spoil_2:
                VNDetailsActivity.spoilerLevel = 2;
                Utils.recreate(context);
                return true;

            default:
                return false;
        }

        VNDBServer.set(type, vn.getId(), fields, context, null, Callback.errorCallback(context));
        MainActivity.instance.getVnlistFragment().refresh();

        return true;
    }

    public void setPopupButton(Button popupButton) {
        this.popupButton = popupButton;
    }
}
