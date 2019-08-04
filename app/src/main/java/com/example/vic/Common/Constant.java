package com.example.vic.Common;

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

    public static final String DELETE_BUTTON = "Delete";
    public static final String CANCEL_BUTTON = "Cancel";

    public static final String IS_IMAGE = "Is Image";
}
