package com.example.vic.Dialog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.DialogClickListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.example.vic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteItemDialogFragment extends DialogFragment {

    private DialogClickListener mDialogClickListener;
    public void setDialogClickListener(DialogClickListener listener){
        mDialogClickListener = listener;
    }

    // Widgets
    private Button mCancelButton, mDeleteButton;

    // Custom References
    private MediaFiles mMediaFile;

    public DeleteItemDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_item_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCancelable(false);

        initView(view);
        clickOnView();

        if(getArguments() != null){
            mMediaFile = (MediaFiles) getArguments().getSerializable(Constant.MEDIA_FILE);
        }

    }

    // End Point: Initialize Views
    private void initView(View view) {
        mDeleteButton = view.findViewById(R.id.delete_btn);
        mCancelButton = view.findViewById(R.id.cancel_btn);
    }

    // End Point: Trigger Action when a view is clicked
    private void clickOnView() {
        mDeleteButton.setOnClickListener(view -> {
            mDialogClickListener.onButtonClicked(Constant.DELETE_BUTTON, null,mMediaFile);
            dismiss();
        });

        mCancelButton.setOnClickListener(view -> {
            mDialogClickListener.onButtonClicked(Constant.CANCEL_BUTTON, null, null);
            dismiss();
        });

    }

}
