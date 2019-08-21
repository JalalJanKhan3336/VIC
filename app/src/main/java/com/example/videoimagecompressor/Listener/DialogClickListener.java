package com.example.videoimagecompressor.Listener;

import com.example.videoimagecompressor.Model.MediaFiles;

public interface DialogClickListener {
    void onButtonClicked(String whichButton, String fileType, MediaFiles item);
}
