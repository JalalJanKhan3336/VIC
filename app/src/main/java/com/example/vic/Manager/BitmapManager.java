package com.example.vic.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.CompressorListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class BitmapManager {

    @SuppressLint("StaticFieldLeak")
    private static BitmapManager mBitmapManager;

    public static BitmapManager with(Context context){
        if(mBitmapManager == null)
            mBitmapManager = new BitmapManager(context);

        return mBitmapManager;
    }

    private CompressorListener mCompressorListener;

    public void setCompressorListener(CompressorListener listener){
        if(listener != null)
            mCompressorListener = listener;
    }


    private Context mContext;

    private BitmapManager(Context context){
        mContext = context;
        checkFolder();
    }

    private void checkFolder() {
        if(!Constant.VIC_FOLDER.exists())
            Constant.VIC_FOLDER.mkdir();
    }

    // End Point: Saving Image into Storage
    public String saveFile(Bitmap image, String imageName, File destination){
        File imageFile = new File(destination, imageName);

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

    // End Point: Saving Video into Storage
    public String saveFile(Context context, Uri videoUri, String videoName, File destination){

        try {
            AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(videoUri, "r");

            if (videoAsset != null) {
                FileInputStream fin = videoAsset.createInputStream();

                File videoFile = new File(destination, videoName);

                if(videoFile.exists())
                    videoFile.delete();

                OutputStream out = new FileOutputStream(videoFile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = fin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                fin.close();
                out.close();

                return videoFile.getAbsolutePath();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

    // Get All Images/Videos' Paths from Specified folder
    public List<String> getAllMediaFiles(File destination){
        List<String> paths = new ArrayList<>();
        File[] files;

        checkFolder();

        if(destination.isDirectory()){
            files = destination.listFiles();

            if(files != null){
                for (File myFile : files) {
                    if(myFile.exists())
                        paths.add(myFile.getAbsolutePath());
                }
            }

        }

        return paths;
    }

    public void compress(boolean isImage, String filePath, File destination) {
        if(isImage){
            String newPath = SiliCompressor.with(mContext).compress(filePath, destination);

            mCompressorListener.onFileCompressed(Constant.IMAGE,extractMediaDetails(newPath, true));

        }else
            new VideoCompressor().execute(filePath);
    }

    // End Point: Extract Image/Video Details
    public MediaFiles extractMediaDetails(String newPath, boolean isImage) {

        // Extract File Properties
        File file = new File(newPath);

        String fileName = getFileName(newPath);
        String filePath = getFileAbsPath(newPath);
        String extension = getFileExtension(newPath);
        double sizeInMB = getFileSize(newPath);
        Uri fileUri = Uri.fromFile(file);

        if(isImage)
            return new ImageFile(filePath, fileName, sizeInMB, Constant.IMAGE, extension, fileUri);
        else
            return new VideoFile(filePath, fileName, sizeInMB, Constant.VIDEO, extension, fileUri);
    }

    // End Point: Extract Image/Video Details
    public MediaFiles extractMediaDetails(String newPath) {

        // Extract File Properties
        File file = new File(newPath);

        String fileName = getFileName(newPath);
        String filePath = getFileAbsPath(newPath);
        String extension = getFileExtension(newPath);
        double sizeInMB = getFileSize(newPath);
        Uri fileUri = Uri.fromFile(file);

        return new MediaFiles(filePath,fileName,sizeInMB,"",extension, fileUri);
    }

    private String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    private String getFileAbsPath(String filePath) {
        File file = new File(filePath);
        return file.getAbsolutePath();
    }

    private String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf("."));
    }

    private double getFileSize(String filePath) {
        File file = new File(filePath);

        long sizeInKB = file.length() / 1024;

        return (double) sizeInKB / 1024;
    }

    private boolean isSDCardSupported(){
        Boolean isSDPresent = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        return isSDPresent && isSDSupportedDevice;
    }

    // Inner Async Class to Compress Video File
    @SuppressLint("StaticFieldLeak")
    private class VideoCompressor extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... paths) {
            String newPath = null;
            String filePath = paths[0];

            try {
                newPath = SiliCompressor.with(mContext).compressVideo(filePath,Constant.VIC_FOLDER.toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return newPath;
        }

        @Override
        protected void onPostExecute(String newPath) {
            super.onPostExecute(newPath);
            mCompressorListener.onFileCompressed(Constant.VIDEO, extractMediaDetails(newPath, false));
        }
    }

}
