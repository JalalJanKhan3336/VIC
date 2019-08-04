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
import com.example.vic.Model.VideoFile;
import com.example.vic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteItemDialogFragment extends DialogFragment {

    private static DeleteItemDialogFragment mInstance;

    public static DeleteItemDialogFragment getInstance(){
        if(mInstance == null)
            mInstance = new DeleteItemDialogFragment();

        return mInstance;
    }

    private DialogClickListener mDialogClickListener;
    public void setDialogClickListener(DialogClickListener listener){
        mDialogClickListener = listener;
    }

    // Widgets
    private Button mCancelButton, mDeleteButton;

    // Custom References
    private ImageFile mImageFile;
    private VideoFile mVideoFile;

    // Local Var
    private boolean mIsImage = true;

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
            mIsImage = getArguments().getBoolean(Constant.IS_IMAGE);

            if(mIsImage){
                mImageFile = (ImageFile) getArguments().getSerializable(Constant.IMAGE);
            }else {
                mVideoFile = (VideoFile) getArguments().getSerializable(Constant.VIDEO);
            }
        }

    }

    // End Point: Initialize Views
    private void initView(View view) {
        mDeleteButton = view.findViewById(R.id.delete_btn);
        mCancelButton = view.findViewById(R.id.cancel_btn);
    }

    // End Point: Trigger Action when a view is clicked
    private void clickOnView() {
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsImage)
                    mDialogClickListener.onButtonClicked(Constant.DELETE_BUTTON, Constant.IMAGE, mImageFile);
                else
                    mDialogClickListener.onButtonClicked(Constant.DELETE_BUTTON, Constant.VIDEO, mVideoFile);

                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogClickListener.onButtonClicked(Constant.CANCEL_BUTTON, null, null);
                dismiss();
            }
        });

    }

}
