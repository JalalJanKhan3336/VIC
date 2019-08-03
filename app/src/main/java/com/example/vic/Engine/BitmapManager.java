package com.example.vic.Engine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapManager {

    @SuppressLint("StaticFieldLeak")
    private static BitmapManager mBitmapManager;

    public static BitmapManager with(Context context){
        if(mBitmapManager == null)
            mBitmapManager = new BitmapManager(context);

        return mBitmapManager;
    }

    private Context mContext;
    private final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();
    private final File VIC_FOLDER = new File(ROOT_DIR+"/VIC");
    private final File IMAGE_FOLDER = new File(VIC_FOLDER+"/Images");
    private final File VIDEO_FOLDER = new File(VIC_FOLDER+"/Videos");


    private BitmapManager(Context context){
        mContext = context;
        checkFolder();
    }

    private void checkFolder() {
        if(!VIC_FOLDER.exists())
            VIC_FOLDER.mkdir();

        if(!IMAGE_FOLDER.exists())
            IMAGE_FOLDER.mkdir();

        if(!VIDEO_FOLDER.exists())
            VIDEO_FOLDER.mkdir();
    }

    public String saveFile(Bitmap image, String imageName){
        File imageFile = new File(IMAGE_FOLDER, imageName);

        if(imageFile.exists())
            imageFile.delete();

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }

    public boolean deleteThisFile(String filePath){
        File file = new File(filePath);

        if(file.exists()) {
            if (file.delete()) {
                Log.d("FileDeleteSuccess", "File Deleted Successfully!");
                return true;
            } else {
                Log.d("FileDeleteFailure", "Unable to Delete File!");
                return false;
            }
        }

        return false;
    }

    private boolean isSDCardSupported(){
        Boolean isSDPresent = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        return isSDPresent && isSDSupportedDevice;
    }

}
