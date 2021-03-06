package com.example.videoimagecompressor.Model;

import android.net.Uri;

import com.example.videoimagecompressor.Utils.SystemUtils;

import java.io.Serializable;

public class MediaFiles implements Serializable {

    private String mFileName, mFileType, mFilePath, mFileExtension;
    private String mFileCompressionDate, mFileCompressionTime;
    private Uri mFileUri;
    private double mFileSizeInMB;

    public MediaFiles() {}

    public MediaFiles(String mFilePath, String mFileName, double mFileSizeInMB,
                     String mFileType, String mFileExtension, Uri mFileUri) {

        this.mFilePath = mFilePath;
        this.mFileName = mFileName;
        this.mFileSizeInMB = mFileSizeInMB;
        this.mFileType = mFileType;
        this.mFileExtension = mFileExtension;
        this.mFileUri = mFileUri;

        this.mFileCompressionDate = SystemUtils.getCurrentDate();
        this.mFileCompressionTime = SystemUtils.getCurrentTime(false);
    }

    public MediaFiles(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType,
                      String mFileExtension, String mFileCompressionDate, String mFileCompressionTime, Uri mFileUri) {

        this.mFilePath = mFilePath;
        this.mFileName = mFileName;
        this.mFileSizeInMB = mFileSizeInMB;
        this.mFileType = mFileType;
        this.mFileExtension = mFileExtension;
        this.mFileCompressionDate = mFileCompressionDate;
        this.mFileCompressionTime = mFileCompressionTime;
        this.mFileUri = mFileUri;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public double getmFileSizeInMB() {
        return mFileSizeInMB;
    }

    public String getmFileExtension() {
        return mFileExtension;
    }

    public void setmFileExtension(String mFileExtension) {
        this.mFileExtension = mFileExtension;
    }

    public void setmFileSizeInMB(double mFileSizeInMB) {
        this.mFileSizeInMB = mFileSizeInMB;
    }

    public String getmFileType() {
        return mFileType;
    }

    public void setmFileType(String mFileType) {
        this.mFileType = mFileType;
    }

    public String getmFileCompressionDate() {
        return mFileCompressionDate;
    }

    public void setmFileCompressionDate(String mFileCompressionDate) {
        this.mFileCompressionDate = mFileCompressionDate;
    }

    public String getmFileCompressionTime() {
        return mFileCompressionTime;
    }

    public void setmFileCompressionTime(String mFileCompressionTime) {
        this.mFileCompressionTime = mFileCompressionTime;
    }

    public Uri getmFileUri() {
        return mFileUri;
    }

    public void setmFileUri(Uri mFileUri) {
        this.mFileUri = mFileUri;
    }

}
