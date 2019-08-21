package com.example.videoimagecompressor.Listener;

import android.view.View;
import com.example.videoimagecompressor.Model.MediaFiles;

public interface ItemClickListener {
    void onItemClicked(MediaFiles mediaFile, View item, int position);
    void onItemLongClicked(MediaFiles mediaFile, View item, int position);
}
