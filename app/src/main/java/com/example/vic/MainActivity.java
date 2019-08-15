package com.example.vic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.vic.Adapters.MediaFilesAdapter;
import com.example.vic.Common.Constant;
import com.example.vic.Dialog.ChooseActionFragment;
import com.example.vic.Dialog.CompressorDialogFragment;
import com.example.vic.Dialog.DeleteItemDialogFragment;
import com.example.vic.Dialog.LoadingDialogFragment;
import com.example.vic.Engine.JJKGlide4Engine;
import com.example.vic.Listener.CompressorListener;
import com.example.vic.Listener.LoadingDialogListener;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Listener.ChooseActionListener;
import com.example.vic.Listener.DialogClickListener;
import com.example.vic.Listener.ItemClickListener;
import com.example.vic.Manager.PermissionManager;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.example.vic.Utils.MessageUtils;
import com.example.vic.Utils.MoverUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ItemClickListener, DialogClickListener,
        ChooseActionListener, CompressorListener,
        PermissionManager.AllPermissionsGrantedListener,
        LoadingDialogListener {

    // Final Var
    private static final int BROWSE_IMAGE_CODE  = 2;
    private static final int BROWSE_VIDEO_CODE  = 4;
    private static final int DARK_GRAY_COLOR = Color.parseColor("#333333");

    // Views
    private Toolbar mToolbar;
    //private Menu mMainMenu;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mNewCompressionFAB;

    // Local Vars
    private boolean mIsImage = true;
    private MediaFiles mMediaFile;
    private List<MediaFiles> mSelectedMediaFiles;

    // Custom References
    private List<MediaFiles> mMediaFileList;
    private BitmapManager mBitmapManager;
    private PermissionManager mPermissionManager;

    // Adapters Ref
    private MediaFilesAdapter mMediaFilesAdapter;

    // Fragment References
    private DeleteItemDialogFragment mDeleteDialogFragment;
    private ChooseActionFragment mChooseActionFragment;
    private LoadingDialogFragment mLoadingDialogFragment;
    private CompressorDialogFragment mCompressorDialogFragment;
    private static int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout);

        initView();
        initRef();
        clickOnButton();
        setUpToolbar();

        mPermissionManager.askPermissions();
    }

    // End Point: Initialize Views
    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mNewCompressionFAB = findViewById(R.id.new_compression_fab);
    }

    // End Point: Initialize References
    private void initRef() {
        mMediaFileList = new ArrayList<>();
        mSelectedMediaFiles = new ArrayList<>();

        mBitmapManager = BitmapManager.with(this);
        mBitmapManager.setCompressorListener(this);

        mPermissionManager = PermissionManager.getInstance(this);
        mPermissionManager.setListener(this);

        mChooseActionFragment = ChooseActionFragment.with();
        mChooseActionFragment.setChooseActionListener(this);

        if(mDeleteDialogFragment == null){
            mDeleteDialogFragment = new DeleteItemDialogFragment();
            mDeleteDialogFragment.setDialogClickListener(this);
        }

        if(mCompressorDialogFragment == null){
            mCompressorDialogFragment = new CompressorDialogFragment();
            mCompressorDialogFragment.setDialogClickListener(this);
        }

        if(mLoadingDialogFragment == null){
            mLoadingDialogFragment = new LoadingDialogFragment();
            mLoadingDialogFragment.setLoadingDialogListener(this);
        }

    }

    // End Point: Trigger Action when FAB is clicked
    private void clickOnButton() {
        mNewCompressionFAB.setOnClickListener(view -> mChooseActionFragment.show(getSupportFragmentManager(), mChooseActionFragment.getTag()));
    }

    // End Point: Setting up Toolbar
    private void setUpToolbar() {
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.app_name));
            mToolbar.setTitle(getString(R.string.app_name));
            mToolbar.setTitleTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateList();
    }

    // End Point: Populating List of Image & Video Files
    private void populateList() {
        List<String> filesPath = mBitmapManager.getAllMediaFiles(Constant.VIC_FOLDER);

        if(mMediaFileList != null)
            mMediaFileList.clear();

        if(filesPath != null){

            for(String path : filesPath){
                MediaFiles file = mBitmapManager.extractMediaDetails(path);
                if(file != null){
                    mMediaFileList.add(file);
                }
            }

        }

        populateRecyclerView();
    }

    // End Point: Populate RecyclerView
    private void populateRecyclerView(){

        if(mMediaFilesAdapter == null)
            mMediaFilesAdapter = new MediaFilesAdapter(this, mMediaFileList,this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(false);
        llm.setReverseLayout(false);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mMediaFilesAdapter);
    }

    @Override
    public void onItemClicked(MediaFiles mediaFile, View item, int position) {
        if(mSelectedMediaFiles.size() > 0){

            if(mSelectedMediaFiles.contains(mediaFile)){
                item.setBackgroundColor(DARK_GRAY_COLOR);
                mSelectedMediaFiles.remove(mediaFile);
                mCounter--;
            }else {
                item.setBackgroundColor(Color.GRAY);
                mSelectedMediaFiles.add(mediaFile);
                mCounter++;
            }

            item.setOnCreateContextMenuListener(this);

        }else {
            Constant.mTempFile = mediaFile;
            MoverUtils.moveTo(MainActivity.this, MediaActivity.class);
            //move(mediaFile);
        }
    }

    @Override
    public void onItemLongClicked(MediaFiles mediaFile, View item, int position) {

        if(mSelectedMediaFiles.size() <= 0){
            item.setBackgroundColor(Color.GRAY);
            mSelectedMediaFiles.add(mediaFile);
            mCounter++;
        }else {
            if(mSelectedMediaFiles.contains(mediaFile)){
                item.setBackgroundColor(DARK_GRAY_COLOR);
                mSelectedMediaFiles.remove(mediaFile);
                mCounter--;
            }else {
                item.setBackgroundColor(Color.GRAY);
                mSelectedMediaFiles.add(mediaFile);
                mCounter++;
            }
        }

        item.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(mCounter +" Items Selected");
        getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onItemSwiped(MediaFiles mediaFile, ItemTouchHelper.SimpleCallback itemSwipedCallback) {
        new ItemTouchHelper(itemSwipedCallback).attachToRecyclerView(mRecyclerView);
        List<MediaFiles> mediaFiles = new ArrayList<>();
        mediaFiles.add(mediaFile);
        delete(mediaFiles);
    }

    @Override
    public void onButtonClicked(String whichButton, String fileType, MediaFiles item) {

        if(whichButton.equals(Constant.DELETE_BUTTON)){
            int deletedItems = 0;

            for(MediaFiles file : mSelectedMediaFiles){

                if(mBitmapManager.deleteThisFile(file.getmFilePath())){
                    int position = mMediaFileList.indexOf(file);

                    mMediaFileList.remove(file);
                    mMediaFilesAdapter.notifyItemRemoved(position);
                    mMediaFilesAdapter.notifyDataSetChanged();
                    deletedItems++;
                }

            }

            if(deletedItems > 0)
                MessageUtils.displaySnackbar(mRecyclerView,deletedItems+" Files deleted successfully!");
            else
                MessageUtils.displaySnackbar(mRecyclerView,"Unable to delete a single File");

        }else if(whichButton.equals(Constant.COMPRESS_BUTTON) && fileType != null){

            if(fileType.equals(Constant.IMAGE)){
                showLoadingDialog("Please Wait","Compressing file, please wait...", null);
                mBitmapManager.compress(true, item.getmFilePath(), Constant.VIC_FOLDER);
            }
            if(fileType.equals(Constant.VIDEO)){
                showLoadingDialog("Please Wait","Compressing file, please wait...", null);
                mBitmapManager.compress(false, item.getmFilePath(), Constant.VIC_FOLDER);
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

    // End Point: Setup Bundle & Move to Media Activity
    private void move(MediaFiles mediaFile) {
        Intent intent = new Intent(MainActivity.this, MediaActivity.class);
        intent.putExtra(Constant.MEDIA_FILE, mediaFile);
        startActivity(intent);
    }

    // End Point: Capture single Image
    private void captureImage() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    // End Point: Browse single Image
    private void browseImage() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new JJKGlide4Engine())
                .forResult(BROWSE_IMAGE_CODE);
    }

    // End Point: Capture single Video
    private void captureVideo() {
        new VideoPicker.Builder(this)
                .mode(VideoPicker.Mode.CAMERA)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    // End Point: Browse single Video
    private void browseVideo() {
        Matisse.from(this)
                .choose(MimeType.ofVideo())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new JJKGlide4Engine())
                .forResult(BROWSE_VIDEO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if(data != null){
                // Local References
                String mSelectedImagePath;
                String mSelectedVideoPath;
                if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE) {
                    mIsImage = true;
                    List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);

                    if(mPaths != null){
                        mSelectedImagePath = mPaths.get(0);
                        workWithImage(mSelectedImagePath);
                    }

                } else if(requestCode == BROWSE_IMAGE_CODE){
                    mIsImage = true;

                    List<String> paths = Matisse.obtainPathResult(data);

                    mSelectedImagePath = paths.get(0);
                    workWithImage(mSelectedImagePath);

                } else if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE) {
                    List<String> mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);

                    if (mPaths != null) {
                        mIsImage = false;

                        mSelectedVideoPath = mPaths.get(0);
                        File file = new File(mSelectedVideoPath);
                        Uri videoUri = Uri.fromFile(file);
                        workWithVideo(mSelectedVideoPath, videoUri);
                    }

                } else if(requestCode == BROWSE_VIDEO_CODE){
                    mIsImage = false;

                    List<String> paths = Matisse.obtainPathResult(data);

                    mSelectedVideoPath = paths.get(0);
                    File file = new File(mSelectedVideoPath);
                    Uri videoUri = Uri.fromFile(file);
                    workWithVideo(mSelectedVideoPath, videoUri);
                }
            }

        }else if(resultCode == RESULT_CANCELED){
            MessageUtils.displayToast(MainActivity.this, "Request is Cancelled");
        }

    }

    // End Point: Working with Selected image via path
    private void workWithImage(String imagePath) {
        mMediaFile = mBitmapManager.extractMediaDetails(imagePath, true);
        mIsImage = true;
        showCompressorDialog(true, Constant.IMAGE, mMediaFile);
    }

    // End Point: Working with Selected video via path
    private void workWithVideo(String videoPath, Uri videoUri) {
        mMediaFile = mBitmapManager.extractMediaDetails(videoPath, false);
        mMediaFile.setmFileUri(videoUri);
        mIsImage = false;
        showCompressorDialog(false, Constant.VIDEO, mMediaFile);
    }

    // End Point: Sending Compressor Dialog a Bundle & Show it
    private void showCompressorDialog(boolean isImage, String key, MediaFiles obj) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_IMAGE, isImage);
        bundle.putSerializable(key, obj);
        mCompressorDialogFragment.setArguments(bundle);
        mCompressorDialogFragment.show(getSupportFragmentManager(),
                mCompressorDialogFragment.getTag());
    }

    @Override
    public void allGranted(boolean yesAllGranted) {
        if(!yesAllGranted)
            mPermissionManager.askPermissions();
    }

    @Override
    public void onFileCompressed(String fileType, MediaFiles file) {
        if(fileType != null){

            mLoadingDialogFragment.dismiss();

            if(fileType.equals(Constant.IMAGE) && file != null){
                mIsImage = true;
                mMediaFile = file;
                showLoadingDialog(Constant.SUCCESS,"File Compression Successful...", Constant.SUCCESS);

            }else if(fileType.equals(Constant.VIDEO) && file != null){
                mIsImage = false;
                mMediaFile = file;
                showLoadingDialog(Constant.SUCCESS,"File Compression Successful...", Constant.SUCCESS);

            }else{
                showLoadingDialog(Constant.FAILURE,"File Compression Failed...",  Constant.FAILURE);
            }

        }

    }

    // End Point: Show dialog box to rename your file
    private void askName(boolean isImage, MediaFiles file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.file_name));
        builder.setMessage(getString(R.string.file_name_msg));

        EditText field = new EditText(this);

        builder.setView(field);

        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            if(!TextUtils.isEmpty(field.getText().toString().trim())){

                String name = field.getText().toString().trim();
                saveFileToStorage(isImage, file, name);

                dialogInterface.dismiss();
                updateList(file);

            }else {
                field.setError("Name your file first");
            }

        });

        builder.setNeutralButton("Override", (dialogInterface, i) -> {
            saveFileToStorage(isImage, file, file.getmFileName());

            dialogInterface.dismiss();
            updateList(file);
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // End Point: Updating Media File list & RecyclerView Adapter
    private void updateList(MediaFiles file) {
        mMediaFileList.remove(file);
        mMediaFilesAdapter.notifyItemRemoved(mMediaFileList.indexOf(file));

        mMediaFileList.add(file);
        mMediaFilesAdapter.notifyItemInserted(mMediaFileList.indexOf(file));
        mMediaFilesAdapter.notifyItemChanged(mMediaFileList.indexOf(file));
    }

    private void saveFileToStorage(boolean isImage, MediaFiles file, String fileName){

        if(isImage){

            ImageFile imageFile = (ImageFile) file;
            Bitmap image = BitmapFactory.decodeFile(imageFile.getmFilePath());
            String fileNewPath = mBitmapManager.saveFile(image, fileName+".jpg", Constant.VIC_FOLDER);

            if(fileNewPath != null){
                MessageUtils.displaySnackbar(mRecyclerView,"Image "+fileName+" is saved successfully");
                mBitmapManager.deleteThisFile(imageFile.getmFilePath());
                imageFile.setmFilePath(fileNewPath);
            }

        }else {

            VideoFile videoFile = (VideoFile) file;

            //String videoOldName = videoFile.getmFileName();
            String videoOldPath = videoFile.getmFilePath();

            videoFile.setmFileName(fileName);
            String fileNewPath = mBitmapManager.saveFile(MainActivity.this,
                    videoFile.getmFileUri(), videoFile.getmFileName()+".mp4",
                    Constant.VIC_FOLDER);

            if(fileNewPath != null){
                MessageUtils.displaySnackbar(mRecyclerView,"Video "+fileName+" is saved successfully");
                videoFile.setmFilePath(fileNewPath);
            }

            mBitmapManager.deleteThisFile(videoOldPath);
        }

    }

    @Override
    public void onSave() {
        if(mIsImage)
            askName(true, mMediaFile);
        else
            askName(false, mMediaFile);
    }

    @Override
    public void onRecompress() {
        mLoadingDialogFragment.reset();
        mLoadingDialogFragment.dismiss();
        showLoadingDialog("Please Wait","Compressing file, please wait...", null);
    }

    private void showLoadingDialog(String title, String msg, String resultType) {

        Bundle bundle = new Bundle();
        bundle.putString(Constant.COMP_RESULT, resultType);
        bundle.putString(Constant.TITLE, title);
        bundle.putString(Constant.MSG, msg);
        mLoadingDialogFragment.setArguments(bundle);
        mLoadingDialogFragment.show(getSupportFragmentManager(), mLoadingDialogFragment.getTag());
    }

    // End Point: Sharing Selected Item
    private void share(MediaFiles mf) {

        File file = new File(mf.getmFilePath());

        Uri uri = Uri.fromFile(file);

        if(uri != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share via"));
        }else {
            MessageUtils.displayToast(this,"Unable to Share this file now");
        }

    }

    // End Point: Deleting Selected Items
    private void delete(List<MediaFiles> files) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.SIZE, files.size());
        mDeleteDialogFragment.setArguments(bundle);
        mDeleteDialogFragment.show(getSupportFragmentManager(), mDeleteDialogFragment.getTag());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        boolean flag = false;

        int id = item.getItemId();

        switch (id){

            case R.id.action_share:{
                if(mSelectedMediaFiles.size() > 0){
                    for(MediaFiles mf : mSelectedMediaFiles){
                        share(mf);
                    }

                    mSelectedMediaFiles.clear();
                }
                else
                    MessageUtils.displayToast(MainActivity.this, "Nothing Selected");

                flag = true;
                break;
            }
            case R.id.action_delete:{
                if(mSelectedMediaFiles.size() > 0){

                    delete(mSelectedMediaFiles);
                }
                else
                    MessageUtils.displayToast(MainActivity.this, "Nothing Selected");

                flag = true;
                break;
            }
            default:
                break;
        }

        return flag;
    }

}
