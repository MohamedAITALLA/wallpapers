package com.aitalla.wallpapersapp.ui.activities;

import static com.aitalla.wallpapersapp.config.AppConfig.privacyPolicy;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.aitalla.wallpapersapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);


        declare();
        init();
    }

    private void declare() {
        toolbar = findViewById(R.id.toolbar_privacy);
        webView = findViewById(R.id.webView);
    }
    private void init() {
        setUpToolBar();
        setUpWebView();
    }

    private void setUpWebView() {
        webView.loadUrl(privacyPolicy);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Privacy Policy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}