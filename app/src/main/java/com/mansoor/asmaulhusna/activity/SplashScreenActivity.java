package com.mansoor.asmaulhusna.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView appLogo;
    TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        appLogo =  findViewById(R.id.appLogo);
        appName =  findViewById(R.id.appName);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                appName.setVisibility(View.VISIBLE);
                appName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class),
                                ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this).toBundle());
                    }
                }, 300);
            }
        }, 1000);

        // Start animation
        appLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up));

    }
}
