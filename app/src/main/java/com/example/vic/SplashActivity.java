package com.example.vic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import com.example.vic.Handler.SharedPrefHandler;
import com.example.vic.Utils.MoverUtils;

public class SplashActivity extends AppCompatActivity {

    private Button mGetStartedButton;
    private ConstraintLayout mSplashLayout;
    private final ConstraintSet mSplashLayoutSet = new ConstraintSet();
    private final ConstraintSet mSplashAltLayoutSet = new ConstraintSet();

    // Custom Ref
    private SharedPrefHandler mSharedPrefHandler;

    // Local Vars
    private boolean mIsAltSplashLayout = false;
    private boolean mIsFirstTime;
    private boolean isButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashLayout = findViewById(R.id.splash_layout);
        mGetStartedButton = findViewById(R.id.get_started_btn);

        mSharedPrefHandler = SharedPrefHandler.getInstance(SplashActivity.this, "VICPref");

        mIsFirstTime = mSharedPrefHandler.getBoolean("FirstTime", true);

        if(!mIsFirstTime){
            MoverUtils.moveTo(SplashActivity.this, MainActivity.class);
            finish();
        }

        mSplashLayoutSet.clone(mSplashLayout);
        mSplashAltLayoutSet.clone(SplashActivity.this, R.layout.activity_splash_alt);

        clickOnButton();


        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateScreen();
                    }
                }, 1000);

    }

    private void clickOnButton() {
        mGetStartedButton.setOnClickListener(view -> {
            isButtonClicked = true;

            mSharedPrefHandler.setBoolean("FirstTime", mIsFirstTime);
            mIsFirstTime = false;
            MoverUtils.moveTo(SplashActivity.this, MainActivity.class);
            finish();
        });
    }

    private void animateScreen() {
        Transition changeBounds = new ChangeBounds();
        changeBounds.setDuration(5000);
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
}
