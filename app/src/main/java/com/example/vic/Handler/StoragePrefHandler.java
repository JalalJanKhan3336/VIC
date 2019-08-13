package com.example.vic.Handler;

public class StoragePrefHandler {
    private static StoragePrefHandler mInstance;

    public static StoragePrefHandler getInstance(){
        if(mInstance == null)
            mInstance = new StoragePrefHandler();

        return mInstance;
    }

    private StoragePrefHandler(){}



}
