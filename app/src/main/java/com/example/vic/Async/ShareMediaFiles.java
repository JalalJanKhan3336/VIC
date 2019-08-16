package com.example.vic.Async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;

import androidx.core.content.FileProvider;

import com.example.vic.Model.MediaFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareMediaFiles extends AsyncTask<List<MediaFiles>, Void, ArrayList<Uri>> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private String mType;

    public ShareMediaFiles(Context mContext, String mType) {
        this.mContext = mContext;
        this.mType = mType;
    }

    @SafeVarargs
    @Override
    protected final ArrayList<Uri> doInBackground(List<MediaFiles>... lists) {
        final ArrayList<Uri> filesUris = new ArrayList<>();

        if(lists != null){
            for(List<MediaFiles> myFiles : lists){
                if(myFiles != null){
                    for(MediaFiles mf : myFiles){
                        if(mf != null){
                            //Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".provider", file);
                            Uri uri = mf.getmFileUri();
                            filesUris.add(uri);
                        }
                    }
                }
            }
        }

        return filesUris;
    }

    @Override
    protected void onPostExecute(ArrayList<Uri> uris) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        shareIntent.setType(mType);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
