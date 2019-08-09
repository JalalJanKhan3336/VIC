package com.example.vic.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.vic.Model.ImageFile;
import com.example.vic.Model.VideoFile;
import com.example.vic.Utils.MessageUtils;
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

    private Context mContext;
    private final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();
    private final File VIC_FOLDER = new File(ROOT_DIR+"/VIC");
    private final File IMAGE_FOLDER = new File(VIC_FOLDER+"/Images");
    private final File VIDEO_FOLDER = new File(VIC_FOLDER+"/Videos");
    private String mCompressedVideoPath = null;
    private boolean isVideoCompressed = false;

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

    // End Point: Saving Image into Storage
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

    // End Point: Saving Video into Storage
    public String saveFile(Context context, Uri videoUri, String videoName){

        try {
            AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(videoUri, "r");

            if (videoAsset != null) {
                FileInputStream fin = videoAsset.createInputStream();

                File videoFile = new File(VIDEO_FOLDER, videoName);

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

    public List<String> getAllFiles(){
        List<String> filesPaths = new ArrayList<>();

        List<String> imagePaths = getAllImages();

        if(imagePaths != null){
            filesPaths.addAll(imagePaths);
        }

        List<String> videoPaths = getAllVideos();

        if(videoPaths != null){
            filesPaths.addAll(videoPaths);
        }

        return filesPaths;
    }

    // Get All Images' Paths from VIC/Images folder
    public List<String> getAllImages(){
        List<String> imagesPaths = new ArrayList<>();
        File[] imageFiles;

        checkFolder();

        if(IMAGE_FOLDER.isDirectory()){
            imageFiles = IMAGE_FOLDER.listFiles();

            if(imageFiles != null){
                for (File imageFile : imageFiles) {
                    imagesPaths.add(imageFile.getAbsolutePath());
                }
            }

        }

        return imagesPaths;
    }


    // Get All Videos' Paths from VIC/Videos folder
    public List<String> getAllVideos(){
        List<String> videosPaths = new ArrayList<>();
        File[] videosPath;

        checkFolder();

        if(VIDEO_FOLDER.isDirectory()){
            videosPath = VIDEO_FOLDER.listFiles();

            if(videosPath != null){
                for (File videoPath : videosPath) {
                    videosPaths.add(videoPath.getAbsolutePath());
                }
            }

        }

        return videosPaths;
    }

    public Object compress(boolean isImage, String filePath) {
        if(isImage){
            String newPath = SiliCompressor.with(mContext).compress(filePath, IMAGE_FOLDER);
            return extractImageDetails(newPath);
        }else {

            new VideoCompressor().execute(filePath);

            if(isVideoCompressed)
                return extractVideoDetails(mCompressedVideoPath);
            else
                return null;
        }
    }

    // End Point: Extract Image Details
    public ImageFile extractImageDetails(String newPath) {

        // Extract File Properties
        File file = new File(newPath);

        String fileName = getFileName(newPath);
        String filePath = getFileAbsPath(newPath);
        String extension = getFileExtension(newPath);
        double sizeInMB = getFileSize(newPath);
        Uri fileUri = Uri.fromFile(file);

        return new ImageFile(filePath, fileName, sizeInMB, extension, fileUri);
    }

    public String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    public String getFileAbsPath(String filePath) {
        File file = new File(filePath);
        return file.getAbsolutePath();
    }

    public String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf("."));
    }

    public double getFileSize(String filePath) {
        File file = new File(filePath);

        long sizeInKB = file.length() / 1024;

        return (double) sizeInKB / 1024;
    }

    // End Point: Extract Video Details
    public VideoFile extractVideoDetails(String newPath) {

        // Extract File Properties
        File file = new File(newPath);

        String fileName = getFileName(newPath);
        String filePath = getFileAbsPath(newPath);
        String extension = getFileExtension(newPath);
        double sizeInMB = getFileSize(newPath);
        Uri fileUri = Uri.fromFile(file);

        return new VideoFile(filePath, fileName, sizeInMB, extension, fileUri);
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
                newPath = SiliCompressor.with(mContext).compressVideo(filePath,VIDEO_FOLDER.toString());
                isVideoCompressed = true;
            } catch (URISyntaxException e) {
                isVideoCompressed = false;
                e.printStackTrace();
            }

            return newPath;
        }

        @Override
        protected void onPostExecute(String newPath) {
            super.onPostExecute(newPath);
            mCompressedVideoPath = newPath;
        }
    }

}
