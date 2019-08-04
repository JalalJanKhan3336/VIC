package com.example.vic.Dialog;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vic.Common.Constant;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Listener.CompressorListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.VideoFile;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompressorDialogFragment extends DialogFragment {

    @SuppressLint("StaticFieldLeak")
    private static CompressorDialogFragment mInstance;

    public static CompressorDialogFragment getInstance(){
        if(mInstance == null)
            mInstance = new CompressorDialogFragment();

        return mInstance;
    }

    private CompressorListener mCompressorListener;

    public void setCompressorListener(CompressorListener listener){
        mCompressorListener = listener;
    }

    // Widgets
    private Button mCancelButton, mCompressButton;
    private ImageView mImageHolder;
    private VideoView mVideoHolder;
    private TextView mFilePropertiesHolder;

    // Custom References
    private ImageFile mImageFile;
    private VideoFile mVideoFile;

    // Local Var
    private boolean mIsImage = true;

    public CompressorDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compressor_dialog, container, false);
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
                populateFields(mImageFile, mIsImage);
            }else {
                mVideoFile = (VideoFile) getArguments().getSerializable(Constant.VIDEO);
            }
        }

    }

    private void populateFields(Object file, boolean isImage) {
        if(isImage){
            ImageFile imageFile = (ImageFile) file;

            mVideoHolder.setVisibility(View.GONE);
            GlideUtils.loadImageAsBitmap(getContext(), imageFile.getmImagePath(), mImageHolder);

            StringBuilder builder = new StringBuilder();

            builder.append(Constant.TITLE + imageFile.getmImageName());
            builder.append(Constant.SIZE + imageFile.getmImageSizeInMB());
            builder.append(Constant.FILE_TYPE + imageFile.getmImageType());
            builder.append(Constant.PATH + imageFile.getmImagePath());

            mFilePropertiesHolder.setText(builder);
        }else {

            VideoFile videoFile = (VideoFile) file;

            mVideoHolder.setVideoPath(videoFile.getmVideoPath());

            StringBuilder builder = new StringBuilder();

            builder.append(Constant.TITLE + videoFile.getmVideoName());
            builder.append(Constant.SIZE + videoFile.getmVideoSizeInMB());
            builder.append(Constant.FILE_TYPE + videoFile.getmVideoType());
            builder.append(Constant.PATH + videoFile.getmVideoPath());

            mFilePropertiesHolder.setText(builder);
        }
    }

    // End Point: Initialize Views
    private void initView(View view) {
        mCancelButton = view.findViewById(R.id.cancel_btn);
        mVideoHolder = view.findViewById(R.id.video_holder);
        mImageHolder = view.findViewById(R.id.image_holder);
        mCompressButton = view.findViewById(R.id.compress_btn);
        mFilePropertiesHolder = view.findViewById(R.id.file_properties_tv);
    }

    // End Point: Trigger Action when a view is clicked
    private void clickOnView() {
        mCompressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsImage){
                    mImageFile = (ImageFile) BitmapManager.with(getContext()).compress(true, mImageFile.getmImagePath());
                    mCompressorListener.onFileCompressed(Constant.IMAGE, mImageFile);
                }
                else {
                    mVideoFile = (VideoFile) BitmapManager.with(getContext()).compress(false, mVideoFile.getmVideoPath());
                    mCompressorListener.onFileCompressed(Constant.VIDEO, mVideoFile);
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCompressorListener.onFileCompressed(null, null);
                dismiss();
            }
        });

    }


}
