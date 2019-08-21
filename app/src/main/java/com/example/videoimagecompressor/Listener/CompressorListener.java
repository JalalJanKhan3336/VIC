package com.example.videoimagecompressor.Listener;

import com.example.videoimagecompressor.Model.MediaFiles;

public interface CompressorListener {
    void onFileCompressed(String fileType, MediaFiles file);
}
