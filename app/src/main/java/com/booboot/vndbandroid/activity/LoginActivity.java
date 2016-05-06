package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static LoginActivity instance;
    public static boolean autologin = true;
    private Button loginButton;
    private EditText loginUsername;
    private EditText loginPassword;
    private ProgressBar progressBar;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsManager.getNoActionBarTheme(this));
        setContentView(R.layout.login);

        instance = this;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(MainActivity.getThemeColor(this, R.attr.colorPrimaryDark));
        }

        signupTextView = (TextView) findViewById(R.id.signupTextView);
        initSignup();
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String savedUsername = SettingsManager.getUsername(this);
        String savedPassword = SettingsManager.getPassword(this);
        if (autologin && savedUsername != null && savedPassword != null) {
            /* Filling the inputs with saved values (for appearance's sake) */
            loginUsername.setText(savedUsername);
            loginPassword.setText(savedPassword);
            autologin = false;

            login();
        } else {
            enableAll();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void initSignup() {
        SpannableString ss = new SpannableString("Don't have a VNDB account yet? Sign up here.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Utils.openInBrowser(LoginActivity.this, Links.VNDB_REGISTER);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, ss.toString().indexOf("Sign up here"), ss.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signupTextView.setText(ss);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance());
        signupTextView.setHighlightColor(Color.TRANSPARENT);
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
        disableAll();
        VNDBServer.login(this, new Callback() {
            @Override
            public void config() {
                Cache.loadData(LoginActivity.this, new Callback() {
                    @Override
                    protected void config() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                });
            }
        }, new Callback() {
            @Override
            public void config() {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                enableAll();
            }
        });
    }

    public void enableAll() {
        progressBar.setVisibility(View.INVISIBLE);
        loginUsername.setEnabled(true);
        loginPassword.setEnabled(true);
        loginButton.setEnabled(true);
    }

    public void disableAll() {
        /* Disabling the inputs */
        loginUsername.setEnabled(false);
        loginPassword.setEnabled(false);
        loginButton.setEnabled(false);
        /* Hiding the keyboard */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressBar.setVisibility(View.VISIBLE);
    }
}
