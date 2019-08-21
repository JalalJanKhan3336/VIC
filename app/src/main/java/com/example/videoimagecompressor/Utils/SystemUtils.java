package com.example.videoimagecompressor.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SystemUtils {

    private SystemUtils(){}

    @SuppressLint("SimpleDateFormat")
    // End Point: Get System's Current Time & Returns it in 12 or 24 Format
    public static String getCurrentTime(boolean is24Format){

        if(is24Format){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            return mdformat.format(calendar.getTime());
        }else{
            String delegate = "hh:mm aaa";
            return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        }
    }

    @SuppressLint("SimpleDateFormat")
    // End Point: Get System's Current Date & Returns it
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        return mdformat.format(calendar.getTime());
    }

    @SuppressLint("MissingPermission")
    public static boolean isConnectedToNetwork(Context context){
        boolean hasWIFI = false;
        boolean hasMobileData = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

            for (NetworkInfo info : networkInfos){
                if(info.getTypeName().equalsIgnoreCase("WIFI")){
                    if(info.isConnected()){
                        hasWIFI = true;
                    }
                }
                else if(info.getTypeName().equalsIgnoreCase("MOBILE")){
                    if(info.isConnected()){
                        hasMobileData = true;
                    }
                }
            }
        }

        return (hasMobileData || hasWIFI);
    }

	
}
