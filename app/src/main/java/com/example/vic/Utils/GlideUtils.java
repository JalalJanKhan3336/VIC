package com.example.vic.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {

    private GlideUtils(){}

    public static void loadImageAsGif(Context context, int image, ImageView holder){
        Glide
                .with(context)
                .asGif()
                .load(image)
                .into(holder);

    }

    public static void loadImageAsBitmap(Context context, int image, ImageView holder){
        Glide
                .with(context)
                .asBitmap()
                .load(image)
                .into(holder);
    }

    public static void loadImageAsBitmap(Context context, String image, ImageView holder){
        Glide
                .with(context)
                .asBitmap()
                .load(image)
                .into(holder);
    }

    public static void loadCircularImageAsBitmap(Context context, String image, ImageView holder){
        Glide
                .with(context)
                .asBitmap()
                .load(image)
                .apply(RequestOptions.circleCropTransform())
                .into(holder);
    }

}
