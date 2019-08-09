package com.example.vic.Dialog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.ChooseActionListener;
import com.example.vic.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseActionFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    // Views
    private ImageView mImageCamera, mVideoCamera, mBrowseImage, mBrowseVideo;

    // Listener Ref
    private ChooseActionListener mChooseActionListener;

    public void setChooseActionListener(ChooseActionListener listener){
        mChooseActionListener = listener;
    }

    private static ChooseActionFragment mInstance;

    public static ChooseActionFragment with(){
        if(mInstance == null)
            mInstance = new ChooseActionFragment();
        return mInstance;
    }

    public ChooseActionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_action, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCancelable(true);

        initView(view);
        clickOnView();
    }

    // End Point: Initialize Views
    private void initView(View view) {
        mImageCamera = view.findViewById(R.id.image_camera);
        mVideoCamera = view.findViewById(R.id.video_camera);
        mBrowseImage = view.findViewById(R.id.browse_image);
        mBrowseVideo = view.findViewById(R.id.browse_video);
    }

    // End Point: Trigger Action When a View is Clicked
    private void clickOnView() {
        mImageCamera.setOnClickListener(this);
        mVideoCamera.setOnClickListener(this);
        mBrowseImage.setOnClickListener(this);
        mBrowseVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.image_camera:{
                mChooseActionListener.onActionChoosed(Constant.IMAGE_CAMERA);
                dismiss();
                break;
            }
            case R.id.video_camera:{
                mChooseActionListener.onActionChoosed(Constant.VIDEO_CAMERA);
                dismiss();
                break;
            }
            case R.id.browse_image:{
                mChooseActionListener.onActionChoosed(Constant.BROWSE_IMAGE);
                dismiss();
                break;
            }
            case R.id.browse_video:{
                mChooseActionListener.onActionChoosed(Constant.BROWSE_VIDEO);
                dismiss();
                break;
            }
            default:
                break;
        }

    }
}
