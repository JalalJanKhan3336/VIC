package com.example.vic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.vic.Adapters.ImageFileAdapter;
import com.example.vic.Adapters.VideoFileAdapter;
import com.example.vic.Common.Constant;
import com.example.vic.Dialog.ChooseActionFragment;
import com.example.vic.Dialog.DeleteItemDialogFragment;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Listener.ChooseActionListener;
import com.example.vic.Listener.DialogClickListener;
import com.example.vic.Listener.ItemClickListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.VideoFile;
import com.example.vic.Utils.MessageUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ItemClickListener, DialogClickListener,
                   ChooseActionListener {

    private static final int CAPTURE_IMAGE_CODE = 1;
    private static final int BROWSE_IMAGE_CODE  = 2;
    private static final int CAPTURE_VIDEO_CODE = 3;
    private static final int BROWSE_VIDEO_CODE  = 4;

    // Views
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mNewCompressionFAB;

    // Custom References
    private List<ImageFile> mImageFileList;
    private List<VideoFile> mVideoFileList;
    private ImageFileAdapter mImageFileAdapter;
    private VideoFileAdapter mVideoFileAdapter;
    private BitmapManager mBitmapManager;

    // Fragment References
    private DeleteItemDialogFragment mDeleteDialogFragment;
    private ChooseActionFragment mChooseActionFragment;

    // Local References
    private String mCompressedSavedImagePath;
    private String mCompressedSavedVideoPath;
    private List<String> mAllFilesPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initRef();
        clickOnButton();
        setUpToolbar();
        populateList();
    }

    // End Point: Initialize Views
    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mNewCompressionFAB = findViewById(R.id.new_compression_fab);
    }

    // End Point: Initialize References
    private void initRef() {
        mImageFileList = new ArrayList<>();
        mVideoFileList = new ArrayList<>();
        mBitmapManager = BitmapManager.with(this);

        mDeleteDialogFragment = DeleteItemDialogFragment.getInstance();
        mDeleteDialogFragment.setDialogClickListener(this);

        mChooseActionFragment = ChooseActionFragment.with();
        mChooseActionFragment.setChooseActionListener(this);

    }

    // End Point: Trigger Action when FAB is clicked
    private void clickOnButton() {
        mNewCompressionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChooseActionFragment.show(getSupportFragmentManager(), mChooseActionFragment.getTag());
            }
        });

    }

    // End Point: Setting up Toolbar
    private void setUpToolbar() {
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

    }

    // End Point: Populating List of Image & Video Files
    private void populateList() {
        List<String> imagesPath = mBitmapManager.getAllImages();

        if(imagesPath != null){
            for(String path : imagesPath){
                ImageFile file = mBitmapManager.extractImageDetails(path);
                if(file != null){
                    mImageFileList.add(file);
                }
            }

            populateRecyclerView(true);
        }

        List<String> videosPath = mBitmapManager.getAllVideos();

        if(videosPath != null){
            for(String path : videosPath){
                VideoFile file = mBitmapManager.extractVideoDetails(path);
                if(file != null){
                    mVideoFileList.add(file);
                }
            }

            populateRecyclerView(false);
        }

    }

    // End Point: Populate RecyclerView
    private void populateRecyclerView(boolean isImageLoader){

        if(isImageLoader){
            if(mImageFileAdapter == null)
                mImageFileAdapter = new ImageFileAdapter(this, mImageFileList, this);

            mRecyclerView.setAdapter(mImageFileAdapter);
        }else {
            if(mVideoFileAdapter == null)
                mVideoFileAdapter = new VideoFileAdapter(this, mVideoFileList, this);

            mRecyclerView.setAdapter(mVideoFileAdapter);
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    public void onItemClicked(String type, Object mediaFile) {
        if(type.equals(Constant.IMAGE)){
            ImageFile imageFile = (ImageFile) mediaFile;

        }else {
            VideoFile videoFile = (VideoFile) mediaFile;
        }
    }

    @Override
    public void onItemLongClicked(String type, Object mediaFile) {
        if(type.equals(Constant.IMAGE)){
            ImageFile imageFile = (ImageFile) mediaFile;

        }else {
            VideoFile videoFile = (VideoFile) mediaFile;
        }
    }

    @Override
    public void onItemSwiped(String type, Object mediaFile, ItemTouchHelper.SimpleCallback itemSwipedCallback) {
        new ItemTouchHelper(itemSwipedCallback).attachToRecyclerView(mRecyclerView);

        if(type.equals(Constant.IMAGE)){
            ImageFile imageFile = (ImageFile) mediaFile;

            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IS_IMAGE, true);
            bundle.putSerializable(Constant.IMAGE, imageFile);
            mDeleteDialogFragment.setArguments(bundle);

            mDeleteDialogFragment.show(getSupportFragmentManager(), mDeleteDialogFragment.getTag());
        }else {
            VideoFile videoFile = (VideoFile) mediaFile;

            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IS_IMAGE, false);
            bundle.putSerializable(Constant.VIDEO, videoFile);
            mDeleteDialogFragment.setArguments(bundle);

            mDeleteDialogFragment.show(getSupportFragmentManager(), mDeleteDialogFragment.getTag());
        }
    }

    @Override
    public void onButtonClicked(String whichButton, String type, Object item) {
        if(type != null && item != null){
           if(whichButton.equals(Constant.DELETE_BUTTON)){
               if(type.equals(Constant.IMAGE)){
                   ImageFile file = (ImageFile) item;

                   if(!mBitmapManager.deleteThisFile(file.getmImagePath())){
                       MessageUtils.displaySnackbar(mRecyclerView,"Unable to delete the File");
                   }else {
                       MessageUtils.displaySnackbar(mRecyclerView,"File deleted successfully!");
                       mImageFileList.remove(file);
                       mImageFileAdapter.notifyDataSetChanged();
                   }

               }else {
                   VideoFile file = (VideoFile) item;

                   if(!mBitmapManager.deleteThisFile(file.getmVideoPath())){
                       MessageUtils.displaySnackbar(mRecyclerView,"Unable to delete the File");
                   }else {
                       MessageUtils.displaySnackbar(mRecyclerView,"File deleted successfully!");
                       mVideoFileList.remove(file);
                       mVideoFileAdapter.notifyDataSetChanged();
                   }

               }
           }
        }
    }

    @Override
    public void onActionChoosed(String actionType) {
        switch (actionType){
            case Constant.IMAGE_CAMERA:{
                captureImage();
                break;
            }
            case Constant.BROWSE_IMAGE:{
                browseImage();
                break;
            }
            case Constant.VIDEO_CAMERA:{
                captureVideo();
                break;
            }
            case Constant.BROWSE_VIDEO:{
                browseVideo();
                break;
            }
            default:
                break;
        }
    }

    // End Point: Capture single Image
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, CAPTURE_IMAGE_CODE);
    }

    // End Point: Browse single Image
    private void browseImage() {

    }

    // End Point: Capture single Video
    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, CAPTURE_VIDEO_CODE);
    }

    // End Point: Browse single Video
    private void browseVideo() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if(data != null){
                if(requestCode == CAPTURE_IMAGE_CODE){
                    Uri uri = data.getData();


                } else if(requestCode == BROWSE_IMAGE_CODE){
                    Uri uri = data.getData();

                } else if(requestCode == CAPTURE_VIDEO_CODE){
                    Uri uri = data.getData();

                } else if(requestCode == BROWSE_VIDEO_CODE){
                    Uri uri = data.getData();

                }
            }

        }else if(resultCode == RESULT_CANCELED){
            MessageUtils.displayToast(MainActivity.this, "Request is Cancelled");
        }

    }
}
