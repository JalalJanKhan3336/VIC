package com.example.vic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.vic.Common.Constant;
import com.example.vic.Dialog.DeleteItemDialogFragment;
import com.example.vic.Listener.DialogClickListener;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.example.vic.Utils.GlideUtils;
import com.example.vic.Utils.MessageUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Objects;

public class MediaActivity extends AppCompatActivity
        implements DialogClickListener {

    // Custom Ref
    private ImageFile mImageFile;
    private VideoFile mVideoFile;
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
        setUpToolbar();

        if(getIntent() != null && getIntent().getExtras() != null){

            MediaFiles mf = (MediaFiles) getIntent().getSerializableExtra(Constant.MEDIA_FILE);

            if(mf != null){
                if(mf.getmFileType().equals(Constant.IMAGE)){
                    setVisibilityStatus(View.VISIBLE, View.GONE);
                    mImageFile = (ImageFile) mf;
                    mIsImage = true;
                    showMediaFile(true);
                }else {
                    setVisibilityStatus(View.GONE, View.VISIBLE);
                    mVideoFile = (VideoFile) mf;
                    mIsImage = false;
                    showMediaFile(false);
                }
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        visibleMenuItems(menu);
        return true;
    }

    private void visibleMenuItems(Menu menu) {
        MenuItem shareItem = menu.getItem(R.id.action_share);
        MenuItem deleteItem = menu.getItem(R.id.action_delete);
        MenuItem selectAllItem = menu.getItem(R.id.action_select_all);

        shareItem.setVisible(true);
        deleteItem.setVisible(true);
        selectAllItem.setVisible(true);
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
        File file;

        if(mIsImage)
            file = new File(mImageFile.getmFilePath());
        else
            file = new File(mVideoFile.getmFilePath());

        Uri uri = Uri.fromFile(file);

        if(uri != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
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
            String title;
            String subTitle;

            if(mIsImage){
                title = mImageFile.getmFileName();
                subTitle = Constant.SIZE + mImageFile.getmFileSizeInMB() + Constant.MB;
            }else{
                title = mImageFile.getmFileName();
                subTitle = Constant.SIZE + mVideoFile.getmFileSizeInMB() + Constant.MB;
            }

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
            GlideUtils.loadImageAsBitmap(this, mImageFile.getmFilePath(), mImageHolder);
        }else {
            setVisibilityStatus(View.GONE, View.VISIBLE);
            mVideoHolder.setZOrderOnTop(true);
            mVideoHolder.setVideoURI(mVideoFile.getmFileUri());
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
        if(whichButton.equals(Constant.DELETE_BUTTON) && fileType.equals(Constant.IMAGE)){
            if(BitmapManager.with(MediaActivity.this).deleteThisFile(mImageFile.getmFilePath())) {
                MessageUtils.displayToast(MediaActivity.this, mImageFile.getmFileName() + " is deleted");
                finish();
            } else
                MessageUtils.displayToast(MediaActivity.this, mImageFile.getmFileName()+" is unable to delete");
        }

        if(whichButton.equals(Constant.DELETE_BUTTON) && fileType.equals(Constant.VIDEO)){
            if(BitmapManager.with(MediaActivity.this).deleteThisFile(mVideoFile.getmFilePath())) {
                MessageUtils.displayToast(MediaActivity.this, mVideoFile.getmFileName() + " is deleted");
                finish();
            } else
                MessageUtils.displayToast(MediaActivity.this, mVideoFile.getmFileName()+" is unable to delete");
        }

        mDeleteItemDialogFragment.dismiss();
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
