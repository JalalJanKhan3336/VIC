package com.example.videoimagecompressor.Utils;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class MoverUtils {

    private MoverUtils(){}

    public static void moveTo(Context fromContext, Class toContext){
        Intent intent = new Intent(fromContext, toContext);
        if(intent.resolveActivity(fromContext.getPackageManager()) != null)
            fromContext.startActivity(intent);
    }

    public static void moveTo(Context fromContext, Class toContext, String key, Object bundle){
        Intent intent = new Intent(fromContext, toContext);
        intent.putExtra(key, (Serializable) bundle);
        if(intent.resolveActivity(fromContext.getPackageManager()) != null)
            fromContext.startActivity(intent);
    }

}
