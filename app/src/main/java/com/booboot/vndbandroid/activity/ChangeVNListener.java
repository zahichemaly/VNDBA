package com.booboot.vndbandroid.activity;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Button;

import com.booboot.vndbandroid.R;

/**
 * Created by od on 20/03/2016.
 */
public class ChangeVNListener implements PopupMenu.OnMenuItemClickListener {
    private Button button;

    public ChangeVNListener(Button button) {
        this.button = button;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        button.setText(item.getTitle());

        switch (item.getItemId()) {
            case R.id.item_playing:
                return true;
            case R.id.item_finished:
                return true;
            case R.id.item_stalled:
                return true;
            case R.id.item_dropped:
                return true;
            case R.id.item_unknown:
                return true;
            case R.id.item_no_status:
                return true;
            case R.id.item_high:
                return true;
            case R.id.item_medium:
                return true;
            case R.id.item_low:
                return true;
            case R.id.item_blacklist:
                return true;
            case R.id.item_no_wishlist:
                return true;
            case R.id.item_10:
                return true;
            case R.id.item_9:
                return true;
            case R.id.item_8:
                return true;
            case R.id.item_7:
                return true;
            case R.id.item_6:
                return true;
            case R.id.item_5:
                return true;
            case R.id.item_4:
                return true;
            case R.id.item_3:
                return true;
            case R.id.item_2:
                return true;
            case R.id.item_1:
                return true;
            case R.id.item_no_vote:
                return true;
            default:
                return false;
        }
    }
}
