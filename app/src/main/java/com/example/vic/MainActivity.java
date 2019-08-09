package com.example.vic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.vic.Adapters.ImageFileAdapter;
import com.example.vic.Adapters.MediaFilesAdapter;
import com.example.vic.Adapters.VideoFileAdapter;
import com.example.vic.Common.Constant;
import com.example.vic.Dialog.ChooseActionFragment;
import com.example.vic.Dialog.CompressorDialogFragment;
import com.example.vic.Dialog.DeleteItemDialogFragment;
import com.example.vic.Engine.JJKGlide4Engine;
import com.example.vic.Listener.CompressorListener;
import com.example.vic.Manager.BitmapManager;
import com.example.vic.Listener.ChooseActionListener;
import com.example.vic.Listener.DialogClickListener;
import com.example.vic.Listener.ItemClickListener;
import com.example.vic.Manager.PermissionManager;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.Model.VideoFile;
import com.example.vic.Utils.MessageUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ItemClickListener, DialogClickListener,
                   ChooseActionListener, CompressorListener,
                   PermissionManager.AllPermissionsGrantedListener{

    // Final Var
    private static final int CAPTURE_IMAGE_CODE = 1;
    private static final int BROWSE_IMAGE_CODE  = 2;
    private static final int CAPTURE_VIDEO_CODE = 3;
    private static final int BROWSE_VIDEO_CODE  = 4;

    // Views
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mNewCompressionFAB;

    // Custom References
    private List<MediaFiles> mMediaFileList;
    private List<ImageFile> mImageFileList;
    private List<VideoFile> mVideoFileList;
    private BitmapManager mBitmapManager;
    private PermissionManager mPermissionManager;
    private CompressorDialogFragment mCompressorDialogFragment;

    // Adapters Ref
    private MediaFilesAdapter mMediaFilesAdapter;
    private ImageFileAdapter mImageFileAdapter;
    private VideoFileAdapter mVideoFileAdapter;

    // Fragment References
    private DeleteItemDialogFragment mDeleteDialogFragment;
    private ChooseActionFragment mChooseActionFragment;

    // Local References
    private String mCompressedSavedImagePath, mSelectedImagePath;
    private String mCompressedSavedVideoPath, mSelectedVideoPath;
    private List<String> mAllFilesPaths;

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
        mImageFileList = new ArrayList<>();
        mVideoFileList = new ArrayList<>();
        mBitmapManager = BitmapManager.with(this);

        mDeleteDialogFragment = DeleteItemDialogFragment.getInstance();
        mDeleteDialogFragment.setDialogClickListener(this);

        mCompressorDialogFragment = CompressorDialogFragment.getInstance();
        mCompressorDialogFragment.setCompressorListener(this);

        mPermissionManager = PermissionManager.getInstance(this);
        mPermissionManager.setListener(this);

        mChooseActionFragment = ChooseActionFragment.with();
        mChooseActionFragment.setChooseActionListener(this);
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
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateList();
    }

    // End Point: Populating List of Image & Video Files
    private void populateList() {
        List<String> imagesPath = mBitmapManager.getAllImages();

        if(mMediaFileList != null)
            mMediaFileList.clear();

        if(imagesPath != null){

            for(String path : imagesPath){
                ImageFile file = mBitmapManager.extractImageDetails(path);
                if(file != null){
                    mMediaFileList.add(file);
                }
            }

        }

        List<String> videosPath = mBitmapManager.getAllVideos();

        if(videosPath != null){

            for(String path : videosPath){
                VideoFile file = mBitmapManager.extractVideoDetails(path);
                if(file != null){
                    mMediaFileList.add(file);
                }
            }
        }

        populateRecyclerView();
    }

    // End Point: Populate RecyclerView
    private void populateRecyclerView(){

//        if(isImageLoader){
//            if(mImageFileAdapter == null)
//                mImageFileAdapter = new ImageFileAdapter(this, mImageFileList, this);
//
//            mRecyclerView.setAdapter(mImageFileAdapter);
//        }else {
//            if(mVideoFileAdapter == null)
//                mVideoFileAdapter = new VideoFileAdapter(this, mVideoFileList, this);
//
//            mRecyclerView.setAdapter(mVideoFileAdapter);
//        }

        if(mMediaFilesAdapter == null)
            mMediaFilesAdapter = new MediaFilesAdapter(this, mMediaFileList,this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        llm.setReverseLayout(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    public void onItemClicked(MediaFiles mediaFile) {

    }

    @Override
    public void onItemLongClicked(MediaFiles mediaFile) {

    }

    @Override
    public void onItemSwiped(MediaFiles mediaFile, ItemTouchHelper.SimpleCallback itemSwipedCallback) {
        new ItemTouchHelper(itemSwipedCallback).attachToRecyclerView(mRecyclerView);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.MEDIA_FILE, mediaFile);
        mDeleteDialogFragment.setArguments(bundle);

        mDeleteDialogFragment.show(getSupportFragmentManager(), mDeleteDialogFragment.getTag());
    }

    @Override
    public void onButtonClicked(String whichButton, MediaFiles item) {
        if(item != null){
           if(whichButton.equals(Constant.DELETE_BUTTON)){

               if(!mBitmapManager.deleteThisFile(item.getmFilePath())){
                   MessageUtils.displaySnackbar(mRecyclerView,"Unable to delete the File");
               }else {
                   MessageUtils.displaySnackbar(mRecyclerView,"File deleted successfully!");
                   mMediaFileList.remove(item);
                   mMediaFilesAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, CAPTURE_VIDEO_CODE);
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
                if(requestCode == CAPTURE_IMAGE_CODE){
                    Uri uri = data.getData();

                    if (uri != null) {
                        mSelectedImagePath = uri.toString();
                        workWithImage(mSelectedImagePath);
                    }

                } else if(requestCode == BROWSE_IMAGE_CODE){
                    List<String> paths = Matisse.obtainPathResult(data);

                    mSelectedImagePath = paths.get(0);
                    workWithImage(mSelectedImagePath);

                } else if(requestCode == CAPTURE_VIDEO_CODE){
                    Uri uri = data.getData();

                    if (uri != null) {
                        mSelectedVideoPath = uri.toString();
                        workWithVideo(mSelectedVideoPath, uri);
                    }

                } else if(requestCode == BROWSE_VIDEO_CODE){
                    List<String> paths = Matisse.obtainPathResult(data);
                    List<Uri> uris = Matisse.obtainResult(data);

                    mSelectedVideoPath = paths.get(0);
                    workWithVideo(mSelectedVideoPath, uris.get(0));
                }
            }

        }else if(resultCode == RESULT_CANCELED){
            MessageUtils.displayToast(MainActivity.this, "Request is Cancelled");
        }

    }

    // End Point: Working with Selected image via path
    private void workWithImage(String imagePath) {
        ImageFile imageFile = mBitmapManager.extractImageDetails(imagePath);
        showCompressorDialog(true, Constant.IMAGE, imageFile);
    }

    // End Point: Working with Selected video via path
    private void workWithVideo(String videoPath, Uri videoUri) {
        VideoFile videoFile = mBitmapManager.extractVideoDetails(videoPath);
        videoFile.setmFileUri(videoUri);
        showCompressorDialog(false, Constant.VIDEO, videoFile);
    }

    // End Point: Sending Compressor Dialog a Bundle & Show it
    private void showCompressorDialog(boolean isImage, String key, Object obj) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_IMAGE, isImage);
        bundle.putSerializable(key, (Serializable) obj);
        mCompressorDialogFragment.setArguments(bundle);
        mCompressorDialogFragment.show(getSupportFragmentManager(),
                mCompressorDialogFragment.getTag());
    }

    // End Point: Extract Image Properties by it's path
    private void discoverImageProperties(String imagePath) {
        String fileName = mBitmapManager.getFileName(imagePath);
        String filePath = mBitmapManager.getFileAbsPath(imagePath);
        String extension = mBitmapManager.getFileExtension(imagePath);
        double sizeInMB = mBitmapManager.getFileSize(imagePath);
        Uri fileUri = Uri.parse(imagePath);

        ImageFile imageFile = new ImageFile();

    }

    // End Point: Extract Video Properties by it's path
    private void discoverVideoProperties(String videoPath) {
        String fileName = mBitmapManager.getFileName(videoPath);
        String filePath = mBitmapManager.getFileAbsPath(videoPath);
        String extension = mBitmapManager.getFileExtension(videoPath);
        double sizeInMB = mBitmapManager.getFileSize(videoPath);
        Uri fileUri = Uri.parse(videoPath);

    }

    @Override
    public void allGranted(boolean yesAllGranted) {
        if(!yesAllGranted)
            mPermissionManager.askPermissions();
    }

    @Override
    public void onFileCompressed(String fileType, MediaFiles file) {
        if(fileType != null && file != null){
            if(fileType.equals(Constant.IMAGE)){
                askName(true, file);
            }
            if(fileType.equals(Constant.VIDEO)){
                askName(false, file);
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
                if(isImage){
                    String name = field.getText().toString().trim();

                    ImageFile imageFile = (ImageFile) file;
                    Bitmap image = BitmapFactory.decodeFile(imageFile.getmFilePath());
                    String fileNewPath = mBitmapManager.saveFile(image, name+".jpg");

                    if(fileNewPath != null){
                        MessageUtils.displaySnackbar(mRecyclerView,"Image "+name+" is saved successfully");
                        mBitmapManager.deleteThisFile(imageFile.getmFilePath());
                        imageFile.setmFilePath(fileNewPath);
                    }

                }else {
                    String name = field.getText().toString().trim();

                    VideoFile videoFile = (VideoFile) file;
                    mBitmapManager.deleteThisFile(videoFile.getmFilePath());
                    String fileNewPath = mBitmapManager.saveFile(MainActivity.this, videoFile.getmFileUri(), name+".mp4");

                    if(fileNewPath != null){
                        MessageUtils.displaySnackbar(mRecyclerView,"Video "+name+" is saved successfully");
                        videoFile.setmFilePath(fileNewPath);
                    }

                }

                dialogInterface.dismiss();
                populateList();

            }else {
                field.setError("Name your file first");
            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
