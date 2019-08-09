package com.example.vic.Model;

import android.net.Uri;

public class ImageFile extends MediaFiles {

    public ImageFile() {}

    public ImageFile(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType, Uri mFileUri) {
        super(mFilePath, mFileName, mFileSizeInMB, mFileType, mFileUri);
    }

    public ImageFile(String mFilePath, String mFileName, double mFileSizeInMB, String mFileType, String mFileCompressionDate, String mFileCompressionTime, Uri mFileUri) {
        super(mFilePath, mFileName, mFileSizeInMB, mFileType, mFileCompressionDate, mFileCompressionTime, mFileUri);
    }
}
