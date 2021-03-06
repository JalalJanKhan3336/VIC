package com.example.videoimagecompressor.Dialog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.videoimagecompressor.Common.Constant;
import com.example.videoimagecompressor.Listener.DialogClickListener;
import com.pakistan.compressor.videoimagecompressor.R;

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
    private TextView mMessageHolder;

    public DeleteItemDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_item_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCancelable(true);

        initView(view);
        clickOnView();

        if(getArguments() != null){
            int noOfItems = getArguments().getInt(Constant.SIZE);
            if(noOfItems > 0){
                String msg = "Wanna DELETE "+noOfItems+" items?";
                mMessageHolder.setText(msg);
            }
        }

    }

    // End Point: Initialize Views
    private void initView(View view) {
        mDeleteButton = view.findViewById(R.id.delete_btn);
        mCancelButton = view.findViewById(R.id.cancel_btn);
        mMessageHolder = view.findViewById(R.id.delete_msg_tv);
    }

    // End Point: Trigger Action when a view is clicked
    private void clickOnView() {
        mDeleteButton.setOnClickListener(view -> {
            mDialogClickListener.onButtonClicked(Constant.DELETE_BUTTON, null,null);
            dismiss();
        });

        mCancelButton.setOnClickListener(view -> {
            mDialogClickListener.onButtonClicked(Constant.CANCEL_BUTTON, null, null);
            dismiss();
        });

    }

}
