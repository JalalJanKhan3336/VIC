package com.example.vic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.example.vic.Utils.MoverUtils;
import com.pakistan.compressor.vic.R;

public class SplashActivity extends AppCompatActivity {

    private Button mGetStartedButton;
    private ConstraintLayout mSplashLayout;
    private final ConstraintSet mSplashLayoutSet = new ConstraintSet();
    private final ConstraintSet mSplashAltLayoutSet = new ConstraintSet();

    // Local Vars
    private boolean mIsAltSplashLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashLayout = findViewById(R.id.splash_layout);
        mGetStartedButton = findViewById(R.id.get_started_btn);

        mSplashLayoutSet.clone(mSplashLayout);
        mSplashAltLayoutSet.clone(SplashActivity.this, R.layout.activity_splash_alt);

        clickOnButton();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateScreen();
            }
        }, 1000);
    }

    private void clickOnButton() {
        mGetStartedButton.setOnClickListener(view -> {

            saveScreenStatus();

            MoverUtils.moveTo(SplashActivity.this, MainActivity.class);
            finish();
        });
    }

    private void animateScreen() {
        Transition changeBounds = new ChangeBounds();
        changeBounds.setDuration(2000);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator());

        TransitionManager.beginDelayedTransition(mSplashLayout, changeBounds);

        if(!mIsAltSplashLayout){
            mSplashAltLayoutSet.applyTo(mSplashLayout);
            mIsAltSplashLayout = true;
        }else {
            mSplashLayoutSet.applyTo(mSplashLayout);
            mIsAltSplashLayout = false;
        }
    }

    private void saveScreenStatus(){
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
    }

    private boolean getScreenStatus(){
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        return settings.getBoolean("firstRun", true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!getScreenStatus()){
            MoverUtils.moveTo(SplashActivity.this, MainActivity.class);
            finish();
        }

    }
}
