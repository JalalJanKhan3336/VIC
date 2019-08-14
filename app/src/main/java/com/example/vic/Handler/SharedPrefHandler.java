package com.example.vic.Handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHandler {

    private static SharedPrefHandler mInstance;

    public static SharedPrefHandler getInstance(Context context, String key){
        if(mInstance == null)
            mInstance = new SharedPrefHandler(context, key);

        return mInstance;
    }

    private SharedPreferences mSharedPreferences;

    private SharedPrefHandler(Context context, String key){
        mSharedPreferences = context.getSharedPreferences(key,Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    public void setBoolean(String key, boolean value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key, boolean defaultValue){
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

}
