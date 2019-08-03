package com.example.vic.Model;

public class AllMediaFiles {
    private ImageFile mImageFile;
    private VideoFile mVideoFile;

    public AllMediaFiles() {}

    public AllMediaFiles(ImageFile mImageFile, VideoFile mVideoFile) {
        this.mImageFile = mImageFile;
        this.mVideoFile = mVideoFile;
    }

    public ImageFile getmImageFile() {
        return mImageFile;
    }

    public void setmImageFile(ImageFile mImageFile) {
        this.mImageFile = mImageFile;
    }

    public VideoFile getmVideoFile() {
        return mVideoFile;
    }

    public void setmVideoFile(VideoFile mVideoFile) {
        this.mVideoFile = mVideoFile;
    }
}
