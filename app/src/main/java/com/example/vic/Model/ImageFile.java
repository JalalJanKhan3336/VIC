package com.example.vic.Model;

import android.net.Uri;

import com.example.vic.Utils.SystemUtils;

import java.io.Serializable;

public class ImageFile implements Serializable {

    private String mImagePath, mImageName, mImageSizeInMB, mImageType;
    private String mImageCompressionDate, mImageCompressionTime;
    private Uri mImageUri;

    public ImageFile() {}

    public ImageFile(String mImagePath, String mImageName, String mImageSizeInMB,
                     String mImageType, Uri mImageUri) {

        this.mImagePath = mImagePath;
        this.mImageName = mImageName;
        this.mImageSizeInMB = mImageSizeInMB;
        this.mImageType = mImageType;
        this.mImageUri = mImageUri;

        this.mImageCompressionDate = SystemUtils.getCurrentDate();
        this.mImageCompressionTime = SystemUtils.getCurrentTime(false);
    }

    public ImageFile(String mImagePath, String mImageName, String mImageSizeInMB, String mImageType,
                     String mImageCompressionDate, String mImageCompressionTime, Uri mImageUri) {

        this.mImagePath = mImagePath;
        this.mImageName = mImageName;
        this.mImageSizeInMB = mImageSizeInMB;
        this.mImageType = mImageType;
        this.mImageCompressionDate = mImageCompressionDate;
        this.mImageCompressionTime = mImageCompressionTime;
        this.mImageUri = mImageUri;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getmImageName() {
        return mImageName;
    }

    public void setmImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public String getmImageSizeInMB() {
        return mImageSizeInMB;
    }

    public void setmImageSizeInMB(String mImageSizeInMB) {
        this.mImageSizeInMB = mImageSizeInMB;
    }

    public String getmImageType() {
        return mImageType;
    }

    public void setmImageType(String mImageType) {
        this.mImageType = mImageType;
    }

    public String getmImageCompressionDate() {
        return mImageCompressionDate;
    }

    public void setmImageCompressionDate(String mImageCompressionDate) {
        this.mImageCompressionDate = mImageCompressionDate;
    }

    public String getmImageCompressionTime() {
        return mImageCompressionTime;
    }

    public void setmImageCompressionTime(String mImageCompressionTime) {
        this.mImageCompressionTime = mImageCompressionTime;
    }

    public Uri getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(Uri mImageUri) {
        this.mImageUri = mImageUri;
    }
}
