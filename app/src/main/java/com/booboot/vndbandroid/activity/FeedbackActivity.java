package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

import java.util.Date;

public class FeedbackActivity extends AppCompatActivity {
    private EditText feedbackBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getTheme(this));
        setContentView(R.layout.feedback);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.send_a_feedback);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        feedbackBody = (EditText) findViewById(R.id.feedbackBody);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                break;

            case R.id.action_send:
                Utils.sendEmail(this, "[VNDB Android] Feedback @ " + new Date().toString(), Utils.getDeviceInfo(this) + feedbackBody.getText().toString());
                Callback.showToast(this, "Your feedback has been sent. Thank you!");
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }
}