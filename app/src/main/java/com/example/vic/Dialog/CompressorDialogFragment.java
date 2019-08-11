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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.DialogClickListener;
import com.example.vic.Listener.LoadingDialogListener;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Listener.CompressorListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompressorDialogFragment extends DialogFragment {

    private static CompressorDialogFragment mInstance;

    public static CompressorDialogFragment getInstance(){
        if(mInstance == null)
            mInstance = new CompressorDialogFragment();

        return mInstance;
    }

    // Listener Ref
    private DialogClickListener mDialogClickListener;

    public void setDialogClickListener(DialogClickListener listener){
        mDialogClickListener = listener;
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

        setVisibilityStatus(View.VISIBLE, View.GONE);

        if(getArguments() != null){
            mIsImage = getArguments().getBoolean(Constant.IS_IMAGE);

            if(mIsImage){
                setVisibilityStatus(View.VISIBLE, View.GONE);
                mImageFile = (ImageFile) getArguments().getSerializable(Constant.IMAGE);
                populateFields(mImageFile, true);
            }else {
                setVisibilityStatus(View.GONE, View.VISIBLE);
                mVideoFile = (VideoFile) getArguments().getSerializable(Constant.VIDEO);
                populateFields(mVideoFile, false);
            }
        }

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void populateFields(MediaFiles file, boolean isImage) {
        if(isImage){
            setVisibilityStatus(View.VISIBLE, View.GONE);
            GlideUtils.loadImageAsBitmap(getContext(), file.getmFilePath(), mImageHolder);
        }else {
            setVisibilityStatus(View.GONE, View.VISIBLE);
            mVideoHolder.setVideoURI(file.getmFileUri());

            // Quick fix for Showing VideoView on DialogFragment
            if(getDialog() != null) {
                mVideoHolder.setZOrderOnTop(true);
                Window window = getDialog().getWindow();

                if(window != null){
                    WindowManager.LayoutParams a = window.getAttributes();
                    a.dimAmount = 0;
                    window.setAttributes(a);
                }

            }

            setUpMediaController();
        }

        mTitleHolder.setText(file.getmFileName());
        mSizeHolder.setText(file.getmFileSizeInMB()+Constant.MB);
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

    private void setVisibilityStatus(int status1, int status2){
        mImageHolder.setVisibility(status1);
        mVideoHolder.setVisibility(status2);
    }

}
