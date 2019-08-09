package com.example.vic.Listener;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.vic.Model.MediaFiles;

public interface ItemClickListener {
    void onItemClicked(MediaFiles mediaFile);
    void onItemLongClicked(MediaFiles mediaFile);
    void onItemSwiped(MediaFiles mediaFile, ItemTouchHelper.SimpleCallback itemSwipedCallback);
}
