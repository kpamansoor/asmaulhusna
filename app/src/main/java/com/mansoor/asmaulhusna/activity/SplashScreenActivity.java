package com.mansoor.asmaulhusna.activity;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mansoor.asmaulhusna.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView appLogo,appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        appLogo =  findViewById(R.id.appLogo);
        appName =  findViewById(R.id.appName);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar));

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
                        startActivity(new Intent(SplashScreenActivity.this,LocationActivity.class),
                                ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this).toBundle());
                        finish();
                    }
                }, 1000);
            }
        }, 1000);

        // Start animation
        appLogo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up));

    }
}
