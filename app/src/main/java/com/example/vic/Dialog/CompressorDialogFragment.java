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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vic.Common.Constant;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Listener.CompressorListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;
import com.example.vic.Utils.MessageUtils;

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
    private TextView mTitleHolder, mSizeHolder, mTypeHolder, mPathHolder;

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
                populateFields(mImageFile, true);
            }else {
                mVideoFile = (VideoFile) getArguments().getSerializable(Constant.VIDEO);
                populateFields(mVideoFile, false);
            }
        }

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void populateFields(MediaFiles file, boolean isImage) {
        if(isImage){

            mImageHolder.setVisibility(View.VISIBLE);
            mVideoHolder.setVisibility(View.GONE);

            GlideUtils.loadImageAsBitmap(getContext(), file.getmFilePath(), mImageHolder);

        }else {

            mImageHolder.setVisibility(View.GONE);
            mVideoHolder.setVisibility(View.VISIBLE);

            mVideoHolder.setVideoURI(file.getmFileUri());

            setUpMediaController();
        }


        mTitleHolder.setText(file.getmFileName());
        mSizeHolder.setText(String.format("%.3f",file.getmFileSizeInMB())+Constant.MB);
        mTypeHolder.setText(file.getmFileType());
        mPathHolder.setText(file.getmFilePath());
    }

    // End Point: Applying Controls on selected video
    private void setUpMediaController() {
        MediaController mc = new MediaController(getContext());
        mc.setSaveEnabled(true);
        mc.setFocusable(true);
        mVideoHolder.setMediaController(mc);
        mc.setAnchorView(mVideoHolder);
    }

    // End Point: Initialize Views
    private void initView(View view) {
        mCancelButton = view.findViewById(R.id.cancel_btn);
        mVideoHolder = view.findViewById(R.id.video_holder);
        mImageHolder = view.findViewById(R.id.image_holder);
        mCompressButton = view.findViewById(R.id.compress_btn);
        mTitleHolder = view.findViewById(R.id.title_holder);
        mSizeHolder = view.findViewById(R.id.size_holder);
        mTypeHolder = view.findViewById(R.id.type_holder);
        mPathHolder = view.findViewById(R.id.path_holder);
    }

    // End Point: Trigger Action when a view is clicked
    private void clickOnView() {
        mCompressButton.setOnClickListener(view -> {
            if (mIsImage){
                mImageFile = (ImageFile) BitmapManager.with(getContext()).compress(true, mImageFile.getmFilePath());
                mCompressorListener.onFileCompressed(Constant.IMAGE, mImageFile);
            }
            else {
                mVideoFile = (VideoFile) BitmapManager.with(getContext()).compress(false, mVideoFile.getmFilePath());
                mCompressorListener.onFileCompressed(Constant.VIDEO, mVideoFile);
            }

            dismiss();
        });

        mCancelButton.setOnClickListener(view -> {
            mCompressorListener.onFileCompressed(null, null);
            dismiss();
        });

    }

}
