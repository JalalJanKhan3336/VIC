package com.example.vic.Model;

import android.net.Uri;

public class VideoFile extends MediaFiles {

    public VideoFile() {}

    public VideoFile(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType, Uri mFileUri) {
        super(mFilePath, mFileName, mFileSizeInMB, mFileType, mFileUri);
    }

    public VideoFile(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType, String mFileCompressionDate, String mFileCompressionTime, Uri mFileUri) {
        super(mFilePath, mFileName, mFileSizeInMB, mFileType, mFileCompressionDate, mFileCompressionTime, mFileUri);
    }

}
