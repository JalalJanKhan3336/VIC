package com.example.vic.Listener;

import androidx.recyclerview.widget.ItemTouchHelper;

public interface ItemClickListener {
    void onItemClicked(String type, Object mediaFile);
    void onItemLongClicked(String type, Object mediaFile);
    void onItemSwiped(String type, Object mediaFile, ItemTouchHelper.SimpleCallback itemSwipedCallback);
}
