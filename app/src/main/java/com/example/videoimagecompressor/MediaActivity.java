package com.example.videoimagecompressor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.videoimagecompressor.Common.Constant;
import com.example.videoimagecompressor.Dialog.DeleteItemDialogFragment;
import com.example.videoimagecompressor.Listener.DialogClickListener;
import com.example.videoimagecompressor.Manager.BitmapManager;
import com.example.videoimagecompressor.Model.MediaFiles;
import com.example.videoimagecompressor.Utils.GlideUtils;
import com.example.videoimagecompressor.Utils.MessageUtils;
import com.pakistan.compressor.videoimagecompressor.R;

import java.io.File;

public class MediaActivity extends AppCompatActivity
        implements DialogClickListener {

    // Custom Ref
    private MediaFiles mMediaFile;
    private DeleteItemDialogFragment mDeleteItemDialogFragment;

    // Views
    private ImageView mImageHolder;
    private VideoView mVideoHolder;
    private Toolbar mToolbar;

    private MediaController mMediaController;

    // Local Var
    private boolean mIsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        initView();
        initRef();

        //if(getIntent() != null && getIntent().getExtras() != null){

            //MediaFiles mf = (MediaFiles) getIntent().getSerializableExtra(Constant.MEDIA_FILE);

        mMediaFile = Constant.mTempFile;

        if(mMediaFile != null){
            if(mMediaFile.getmFileType().equals(Constant.IMAGE)){
                setVisibilityStatus(View.VISIBLE, View.GONE);
                mIsImage = true;
                showMediaFile(true);
            }else {
                setVisibilityStatus(View.GONE, View.VISIBLE);
                mIsImage = false;
                showMediaFile(false);
            }
        }

        setUpToolbar();
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean flag = false;

        int id = item.getItemId();

        switch (id){

            case R.id.action_share:{
                share();
                flag = true;

                break;
            }
            case R.id.action_delete:{
                delete();
                flag = true;

                break;
            }
            default:
                break;
        }

        return flag;
    }

    // End Point: Sharing Selected Item
    private void share() {
        File file = new File(mMediaFile.getmFilePath());
        Uri uri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName()+".provider", file);

        if(uri != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share via"));
        }else {
            MessageUtils.displayToast(this,"Unable to Share this file");
        }

    }

    // End Point: Deleting Selected Item
    private void delete() {

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.SIZE, 1);
        mDeleteItemDialogFragment.setArguments(bundle);
        mDeleteItemDialogFragment.show(getSupportFragmentManager(), mDeleteItemDialogFragment.getTag());
    }

    // End Point: Setting Up Toolbar
    private void setUpToolbar() {
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null){
            String title = mMediaFile.getmFileName();
            String subTitle = Constant.SIZE + mMediaFile.getmFileSizeInMB() + Constant.MB;

            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle(subTitle);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mToolbar.setTitleTextColor(Color.WHITE);
            mToolbar.setSubtitleTextColor(Color.WHITE);
        }

    }

    // End Point: Initialize Views
    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mVideoHolder = findViewById(R.id.video_holder);
        mImageHolder = findViewById(R.id.image_holder);
    }

    // End Point: Initialize References
    private void initRef() {
        mDeleteItemDialogFragment = new DeleteItemDialogFragment();
        mDeleteItemDialogFragment.setDialogClickListener(this);
        mMediaController = new MediaController(this);
    }

    private void setVisibilityStatus(int status1, int status2){
        mImageHolder.setVisibility(status1);
        mVideoHolder.setVisibility(status2);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showMediaFile(boolean isImage) {
        if(isImage){
            setVisibilityStatus(View.VISIBLE, View.GONE);
            GlideUtils.loadImageAsBitmap(this, mMediaFile.getmFilePath(), mImageHolder);
        }else {
            setVisibilityStatus(View.GONE, View.VISIBLE);
            mVideoHolder.setZOrderOnTop(true);
            mVideoHolder.setVideoURI(mMediaFile.getmFileUri());
            setUpMediaController();
        }

    }

    // End Point: Applying Controls on selected video
    private void setUpMediaController() {
        mMediaController.setSaveEnabled(true);
        mMediaController.setFocusable(true);
        mVideoHolder.setMediaController(mMediaController);
        mMediaController.setAnchorView(mVideoHolder);
        mVideoHolder.start();
    }

    @Override
    public void onBackPressed() {

        if(!mIsImage){
            stopMedia();
        }

        super.onBackPressed();
    }

    private void stopMedia() {
        if(mMediaController != null && mMediaController.isShowing()){
            mMediaController.removeView(mVideoHolder);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!mIsImage){
            stopMedia();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!mIsImage){
            stopMedia();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!mIsImage){
            stopMedia();
            mMediaController = null;
        }

    }

    @Override
    public void onButtonClicked(String whichButton, String fileType, MediaFiles item) {
        if(whichButton.equals(Constant.DELETE_BUTTON)){
            if(BitmapManager.with(MediaActivity.this).deleteThisFile(mMediaFile.getmFilePath())) {
                MessageUtils.displayToast(MediaActivity.this, mMediaFile.getmFileName() + " is deleted");
                finish();
            } else
                MessageUtils.displayToast(MediaActivity.this, mMediaFile.getmFileName()+" is unable to delete");
        }

        mDeleteItemDialogFragment.dismiss();
    }

}
