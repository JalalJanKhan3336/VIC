package com.example.vic.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MessageUtils {

    public static void displayToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void displaySnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
