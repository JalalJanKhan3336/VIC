package com.example.vic.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.vic.Model.ImageFile;
import com.example.vic.Model.VideoFile;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        if(IMAGE_FOLDER.isDirectory()){
            videosPath = IMAGE_FOLDER.listFiles();

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
            String newPath = null;
            try {
                newPath = SiliCompressor.with(mContext).compressVideo(filePath,VIDEO_FOLDER.toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return extractVideoDetails(newPath);
        }
    }

    // End Point: Extract Image Details
    public ImageFile extractImageDetails(String newPath) {

        // Extract File Properties
        File file = new File(newPath);

        String fileName = file.getName();
        String filePath = file.getAbsolutePath();
        String extension = filePath.substring(filePath.lastIndexOf("."));
        Uri fileUri = Uri.fromFile(file);

        long sizeInKB = file.length() / 1024;
        long sizeInMB = sizeInKB / 1024;

        return new ImageFile(filePath, fileName, String.valueOf(sizeInMB), extension, fileUri);
    }

    // End Point: Extract Video Details
    public VideoFile extractVideoDetails(String newPath) {

        // Extract File Properties
        File file = new File(newPath);

        String fileName = file.getName();
        String filePath = file.getAbsolutePath();
        String extension = filePath.substring(filePath.lastIndexOf("."));
        Uri fileUri = Uri.fromFile(file);

        long sizeInKB = file.length() / 1024;
        long sizeInMB = sizeInKB / 1024;

        return new VideoFile(filePath, fileName, String.valueOf(sizeInMB), extension, fileUri);
    }

    private boolean isSDCardSupported(){
        Boolean isSDPresent = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        return isSDPresent && isSDSupportedDevice;
    }

}
