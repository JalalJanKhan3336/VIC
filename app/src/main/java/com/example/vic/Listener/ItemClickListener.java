package com.example.vic.Listener;

import android.view.View;
import com.example.vic.Model.MediaFiles;

public interface ItemClickListener {
    void onItemClicked(MediaFiles mediaFile, View item, int position);
    void onItemLongClicked(MediaFiles mediaFile, View item, int position);
}
