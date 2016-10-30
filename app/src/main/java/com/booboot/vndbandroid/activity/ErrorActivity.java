/*
 * Copyright 2015 Eduard Ereza Martínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.booboot.vndbandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booboot.vndbandroid.bean.vndbandroid.Theme;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

import java.util.Date;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.R;

public final class ErrorActivity extends AppCompatActivity {
    private String fullErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.THEMES.get(SettingsManager.getTheme(this)).getStyle());
        setContentView(R.layout.customactivityoncrash_default_error_activity);

        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        Button restartButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_restart_button);
        assert restartButton != null;

        final Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
        final CustomActivityOnCrash.EventListener eventListener = CustomActivityOnCrash.getEventListenerFromIntent(getIntent());
        fullErrorMessage = CustomActivityOnCrash.getAllErrorDetailsFromIntent(ErrorActivity.this, getIntent());
        fullErrorMessage = addErrorInfo("Android version: " + Build.VERSION.RELEASE);
        fullErrorMessage = addErrorInfo("VNDB username: " + SettingsManager.getUsername(this));

        if (restartActivityClass != null) {
            restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app);
            restartButton.setOnClickListener(v -> {
                Intent intent = new Intent(ErrorActivity.this, restartActivityClass);
                CustomActivityOnCrash.restartApplicationWithIntent(ErrorActivity.this, intent, eventListener);
            });
        } else {
            restartButton.setOnClickListener(v -> CustomActivityOnCrash.closeApplication(ErrorActivity.this, eventListener));
        }

        Button moreInfoButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_more_info_button);
        assert moreInfoButton != null;

        if (CustomActivityOnCrash.isShowErrorDetailsFromIntent(getIntent())) {
            moreInfoButton.setOnClickListener(v -> {
                //We retrieve all the error data and show it
                AlertDialog dialog = new AlertDialog.Builder(ErrorActivity.this)
                        .setTitle(R.string.customactivityoncrash_error_activity_error_details_title)
                        .setMessage(fullErrorMessage)
                        .setPositiveButton(R.string.customactivityoncrash_error_activity_error_details_close, null)
                        .setNeutralButton(R.string.customactivityoncrash_error_activity_error_details_copy,
                                (dialog1, which) -> {
                                    copyErrorToClipboard();
                                    Toast.makeText(ErrorActivity.this, R.string.customactivityoncrash_error_activity_error_details_copied, Toast.LENGTH_SHORT).show();
                                })
                        .show();

                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));
            });
        } else {
            moreInfoButton.setVisibility(View.GONE);
        }

        int defaultErrorActivityDrawableId = CustomActivityOnCrash.getDefaultErrorActivityDrawableIdFromIntent(getIntent());
        ImageView errorImageView = ((ImageView) findViewById(R.id.customactivityoncrash_error_activity_image));
        assert errorImageView != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId, getTheme()));
        } else {
            //noinspection deprecation
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId));
        }

        Utils.sendEmail(this, "[VNDB Android] Bug @ " + new Date().toString(), fullErrorMessage);
    }

    private String addErrorInfo(String s) {
        int insertPosition = fullErrorMessage.indexOf("Stack trace") - 2;
        return new StringBuilder(fullErrorMessage).insert(insertPosition, s + "\n").toString();
    }

    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(ErrorActivity.this, getIntent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation);
            clipboard.setPrimaryClip(clip);
        } else {
            //noinspection deprecation
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(errorInformation);
        }
    }
}
