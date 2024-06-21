package com.aitalla.wallpapersapp.ui.activities;

import static com.aitalla.wallpapersapp.config.AppConfig.mintegralAppID;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.airbnb.lottie.LottieAnimationView;
import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.utilities.Utilities;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btn = findViewById(R.id.lunch_btn);
        btn.setVisibility(View.GONE);
        LottieAnimationView lottie =  findViewById(R.id.lottie);
        lottie.playAnimation();
        Utilities.readData(this);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
              if(mintegralAppID!=""){
                  lottie.setVisibility(View.GONE);
                  btn.setVisibility(View.VISIBLE);
              }

            }
        }, 5000);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
    }
}