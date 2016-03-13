package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Callback;

import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.settings.SettingsManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton;
    private EditText loginUsername;
    private EditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        if (SettingsManager.getUsername(this) != null && SettingsManager.getPassword(this) != null) {
            login();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            SettingsManager.setUsername(this, loginUsername.getText().toString());
            SettingsManager.setPassword(this, loginPassword.getText().toString());
            login();
        }
    }

    private void login() {
        VNDBServer.login(this, new Callback() {
            @Override
            public void config() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }, new Callback() {
            @Override
            public void config() {
                SettingsManager.setUsername(LoginActivity.this, null);
                SettingsManager.setPassword(LoginActivity.this, null);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
