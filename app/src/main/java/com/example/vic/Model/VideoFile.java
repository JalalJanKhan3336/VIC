package com.example.vic.Model;

import android.net.Uri;

public class VideoFile extends MediaFiles {

    public VideoFile() {}

    public VideoFile(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType, String mFileExtension, Uri mFileUri) {
        super(mFilePath, mFileName, mFileSizeInMB, mFileType, mFileExtension, mFileUri);
    }

    public VideoFile(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType, String mFileExtension,
                     String mFileCompressionDate, String mFileCompressionTime, Uri mFileUri) {
        super(mFilePath, mFileName, mFileSizeInMB, mFileType, mFileExtension, mFileCompressionDate, mFileCompressionTime, mFileUri);
    }

}
