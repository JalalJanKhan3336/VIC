package com.example.videoimagecompressor.Dialog;

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

import com.example.videoimagecompressor.Common.Constant;
import com.example.videoimagecompressor.Listener.DialogClickListener;
import com.example.videoimagecompressor.Model.ImageFile;
import com.example.videoimagecompressor.Model.MediaFiles;
import com.example.videoimagecompressor.Model.VideoFile;
import com.pakistan.compressor.videoimagecompressor.R;
import com.example.videoimagecompressor.Utils.GlideUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompressorDialogFragment extends DialogFragment {

    // Listener Ref
    private DialogClickListener mDialogClickListener;

    public void setDialogClickListener(DialogClickListener listener){
        mDialogClickListener = listener;
    }

    // Widgets
    private Button mCancelButton, mCompressButton;
    private ImageView mImageHolder;
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
                if (mImageFile != null) {
                    populateFields(mImageFile);
                }
            }else {
                mVideoFile = (VideoFile) getArguments().getSerializable(Constant.VIDEO);
                if (mVideoFile != null) {
                    populateFields(mVideoFile);
                }
            }
        }

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void populateFields(MediaFiles file) {
        setVisibilityStatus(View.VISIBLE);
        GlideUtils.loadImageAsBitmap(getContext(), file.getmFilePath(), mImageHolder);

        mTitleHolder.setText(file.getmFileName());
        mSizeHolder.setText(file.getmFileSizeInMB()+Constant.MB);
        mTypeHolder.setText(file.getmFileType());
        mPathHolder.setText(file.getmFilePath());
    }

    // End Point: Initialize Views
    private void initView(View view) {
        mCancelButton = view.findViewById(R.id.cancel_btn);
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
                mDialogClickListener.onButtonClicked(Constant.COMPRESS_BUTTON,Constant.IMAGE, mImageFile);
            }
            else {
                mDialogClickListener.onButtonClicked(Constant.COMPRESS_BUTTON, Constant.VIDEO, mVideoFile);
            }

            dismiss();
        });

        mCancelButton.setOnClickListener(view -> {
            mDialogClickListener.onButtonClicked(Constant.CANCEL_BUTTON, null, null);
            dismiss();
        });

    }

    private void setVisibilityStatus(int status1){
        mImageHolder.setVisibility(status1);
    }

}
