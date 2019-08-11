package com.example.vic.Common;

import android.os.Environment;

import java.io.File;

public abstract class Constant {

    private Constant() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot to instantiate abstract class");
    }

    // Constant Vars
    public static final String TITLE = "Title ";
    public static final String SIZE = "Size ";
    public static final String FILE_TYPE = "File Type ";
    public static final String PATH = "Path ";

    public static final String IMAGE = "Image";
    public static final String VIDEO = "Video";

    public static final String IMAGE_CAMERA = "Image Camera";
    public static final String VIDEO_CAMERA = "Video Camera";
    public static final String BROWSE_IMAGE = "Browse Image";
    public static final String BROWSE_VIDEO = "Browse Video";

    public static final String MB = " MB";
    public static final String MSG = "Message";

    public static final String DELETE_BUTTON = "Delete";
    public static final String CANCEL_BUTTON = "Cancel";

    public static final String COMPRESS_BUTTON = "Compress Button";

    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";
    public static final String COMP_RESULT = "Compression Result";

    public static final String IS_IMAGE = "Is Image";
    public static final String MEDIA_FILE = "Media File";

    public static final String PROCESSING = "Processing";
    public static final String PLZ_WAIT = "Please wait";

    // Storage Folders
    private static final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();
    public  static final File VIC_FOLDER = new File(ROOT_DIR+"/VIC");

}
