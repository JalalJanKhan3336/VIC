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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.LoadingDialogListener;
import com.pakistan.compressor.vic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingDialogFragment extends DialogFragment {

    // Widgets
    private ImageView mIconHolder;
    private ProgressBar mProgressbar;
    private Button mSaveButton, mReCompressButton;
    private TextView mTitleHolder, mMessageHolder;

    // Local Vars
    private String mResultType;

    // Listener Ref
    private LoadingDialogListener mLoadingDialogListener;
    public void setLoadingDialogListener(LoadingDialogListener listener){
        mLoadingDialogListener = listener;
    }

    public LoadingDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCancelable(false);

        initView(view);
        clickOnButton();

        reset();

        if(getArguments() != null){
            String resultType = (String) getArguments().get(Constant.COMP_RESULT);
            String title = (String) getArguments().get(Constant.TITLE);
            String msg = (String) getArguments().get(Constant.MSG);

            if(title == null)
                title = Constant.PROCESSING;

            if(msg == null)
                msg = Constant.PLZ_WAIT;

            if(resultType != null){
                mResultType = resultType;
                if(mResultType.equals(Constant.SUCCESS))
                    onResult(R.drawable.success, title, msg, true);
                else
                    onResult(R.drawable.failure, title, msg, true);
            }else {
                onResult(0, title, msg, false);
            }

        }

    }

    private void initView(View view) {
        mSaveButton = view.findViewById(R.id.save_btn);
        mIconHolder = view.findViewById(R.id.icon_holder);
        mProgressbar = view.findViewById(R.id.progressbar);
        mTitleHolder = view.findViewById(R.id.title_holder);
        mMessageHolder = view.findViewById(R.id.msg_holder);
        mReCompressButton = view.findViewById(R.id.re_compress_btn);
    }

    // End Point: Perform Action against a button is clicked
    private void clickOnButton() {
        mSaveButton.setOnClickListener(view -> {
            dismiss();
            mLoadingDialogListener.onSave();
        });

        mReCompressButton.setOnClickListener(view -> {
            dismiss();
            mLoadingDialogListener.onRecompress();
        });
    }

    private void onResult(int icon, String title, String msg, boolean showIcon){
        if(mResultType != null){

            if(showIcon){

                if(mIconHolder != null){
                    mIconHolder.setVisibility(View.VISIBLE);
                    mIconHolder.setImageResource(icon);
                }

                if(mProgressbar != null)
                    mProgressbar.setVisibility(View.GONE);

            }else {

                if(mIconHolder != null)
                    mIconHolder.setVisibility(View.GONE);

                if(mProgressbar != null)
                    mProgressbar.setVisibility(View.VISIBLE);

            }

            if(mTitleHolder != null)
                mTitleHolder.setText(title);

            if(mMessageHolder != null)
                mMessageHolder.setText(msg);

            if(mSaveButton != null && mReCompressButton != null){
                if(mResultType.equals(Constant.SUCCESS)) {
                    mSaveButton.setVisibility(View.VISIBLE);
                    mReCompressButton.setVisibility(View.GONE);
                }else if(mResultType.equals(Constant.FAILURE)) {
                    mSaveButton.setVisibility(View.GONE);
                    mReCompressButton.setVisibility(View.VISIBLE);
                }else {
                    mSaveButton.setVisibility(View.GONE);
                    mReCompressButton.setVisibility(View.GONE);
                }
            }
        }
    }

    public void reset(){
        if(mIconHolder != null)
            mIconHolder.setVisibility(View.GONE);

        if(mSaveButton != null)
            mSaveButton.setVisibility(View.GONE);

        if(mReCompressButton != null)
            mReCompressButton.setVisibility(View.GONE);

        if(mProgressbar != null)
            mProgressbar.setVisibility(View.VISIBLE);
    }

}
