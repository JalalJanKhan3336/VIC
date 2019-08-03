package com.example.vic.Model;

import android.net.Uri;

import com.example.vic.Utils.SystemUtils;

import java.io.Serializable;

public class VideoFile implements Serializable {

    private String mVideoPath, mVideoName, mVideoSizeInMB, mVideoType;
    private String mVideoCompressionDate, mVideoCompressionTime;
    private Uri mVideoUri;

    public VideoFile() {}

    public VideoFile(String mVideoPath, String mVideoName, String mVideoSizeInMB,
                     String mVideoType, Uri mVideoUri) {

        this.mVideoPath = mVideoPath;
        this.mVideoName = mVideoName;
        this.mVideoSizeInMB = mVideoSizeInMB;
        this.mVideoType = mVideoType;
        this.mVideoUri = mVideoUri;

        this.mVideoCompressionDate = SystemUtils.getCurrentDate();
        this.mVideoCompressionTime = SystemUtils.getCurrentTime(false);
    }

    public VideoFile(String mVideoPath, String mVideoName, String mVideoSizeInMB,
                     String mVideoType, String mVideoCompressionDate,
                     String mVideoCompressionTime, Uri mVideoUri) {

        this.mVideoPath = mVideoPath;
        this.mVideoName = mVideoName;
        this.mVideoSizeInMB = mVideoSizeInMB;
        this.mVideoType = mVideoType;
        this.mVideoCompressionDate = mVideoCompressionDate;
        this.mVideoCompressionTime = mVideoCompressionTime;
        this.mVideoUri = mVideoUri;
    }

    public String getmVideoPath() {
        return mVideoPath;
    }

    public void setmVideoPath(String mVideoPath) {
        this.mVideoPath = mVideoPath;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    public String getmVideoSizeInMB() {
        return mVideoSizeInMB;
    }

    public void setmVideoSizeInMB(String mVideoSizeInMB) {
        this.mVideoSizeInMB = mVideoSizeInMB;
    }

    public String getmVideoType() {
        return mVideoType;
    }

    public void setmVideoType(String mVideoType) {
        this.mVideoType = mVideoType;
    }

    public String getmVideoCompressionDate() {
        return mVideoCompressionDate;
    }

    public void setmVideoCompressionDate(String mVideoCompressionDate) {
        this.mVideoCompressionDate = mVideoCompressionDate;
    }

    public String getmVideoCompressionTime() {
        return mVideoCompressionTime;
    }

    public void setmVideoCompressionTime(String mVideoCompressionTime) {
        this.mVideoCompressionTime = mVideoCompressionTime;
    }

    public Uri getmVideoUri() {
        return mVideoUri;
    }

    public void setmVideoUri(Uri mVideoUri) {
        this.mVideoUri = mVideoUri;
    }
}
