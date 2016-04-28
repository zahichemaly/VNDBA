package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {
    public final static int NUMBER_OF_CREATIONS = 2;
    public static int creationsRemaining = NUMBER_OF_CREATIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationsRemaining--;
        if (creationsRemaining > 0) {
            Intent intent = new Intent(this, EmptyActivity.class);
            startActivity(intent);
        } else {
            creationsRemaining = NUMBER_OF_CREATIONS;
        }
        finish();
    }
}
