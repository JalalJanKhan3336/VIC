package com.example.vic.Listener;

import com.example.vic.Model.MediaFiles;

public interface CompressorListener {
    void onFileCompressed(String fileType, MediaFiles file);
}
