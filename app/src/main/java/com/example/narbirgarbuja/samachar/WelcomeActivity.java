package com.example.narbirgarbuja.samachar;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ObjectAnimator progressAnimator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progressBar = (ProgressBar)findViewById(R.id.progressBar) ;
        progressAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,100);
        progressAnimator.setDuration(4000);
        progressAnimator.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
