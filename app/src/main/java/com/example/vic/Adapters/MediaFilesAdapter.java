package com.example.vic.Adapters;

import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.ItemClickListener;
import com.example.vic.Model.MediaFiles;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.List;

public class MediaFilesAdapter extends RecyclerView.Adapter<MediaFilesAdapter.MediaFileViewHolder> {

    private Context mContext;
    private List<MediaFiles> mMediaFilesList;

    // Interface Reference
    private ItemClickListener mMediaFileListener;

    private static final int AD_TYPE = 1;
    private static final int CONTENT_TYPE = 2;

    public MediaFilesAdapter(Context mContext, List<MediaFiles> mMediaFilesList, ItemClickListener mMediaFileListener) {
        this.mContext = mContext;
        this.mMediaFilesList = mMediaFilesList;
        this.mMediaFileListener = mMediaFileListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if(viewType == AD_TYPE){
            itemView = new AdView(mContext);
            ((AdView) itemView).setAdSize(AdSize.BANNER);
            ((AdView) itemView).setAdUnitId(mContext.getString(R.string.banner_ad_id));

            float density = mContext.getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,height);
            itemView.setLayoutParams(params);

            AdRequest adRequest = new AdRequest.Builder().build();
            ((AdView) itemView).loadAd(adRequest);
        }else {
           itemView = LayoutInflater.from(mContext).inflate(R.layout.media_file_item, parent, false);
        }

        return new MediaFileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaFileViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if(viewType == CONTENT_TYPE){
            MediaFiles obj = mMediaFilesList.get(position);

            GlideUtils.loadCircularImageAsBitmap(mContext, obj.getmFilePath(), holder.mThumbnailHolder);

            DecimalFormat df = new DecimalFormat("#.###");
            double d = Double.valueOf(df.format(obj.getmFileSizeInMB()));

            String size = Constant.SIZE + d + Constant.MB;
            String type = Constant.FILE_TYPE + obj.getmFileExtension();
            String path = Constant.PATH + obj.getmFilePath();

            holder.mSizeHolder.setText(size);
            holder.mTypeHolder.setText(type);
            holder.mPathHolder.setText(path);
            holder.mTitleHolder.setText(obj.getmFileName());
            holder.mDateHolder.setText(obj.getmFileCompressionDate());
            holder.mTimeHolder.setText(obj.getmFileCompressionTime());

            clickOnItem(holder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mMediaFilesList.size();
    }

    // End Point: Trigger Action on RecyclerView item clicked, swipe
    private void clickOnItem(MediaFileViewHolder holder, int position) {
        MediaFiles obj = mMediaFilesList.get(position);

        // When an Item Clicked
        holder.itemView.setOnClickListener(view -> {
            if(obj.getmFileExtension().equals(".mp4")){
                obj.setmFileType(Constant.VIDEO);
            }else {
                obj.setmFileType(Constant.IMAGE);
            }

            mMediaFileListener.onItemClicked(obj, view, position);
        });

        // When an Item Long Clicked
        holder.itemView.setOnLongClickListener(view -> {
            mMediaFileListener.onItemLongClicked(obj, view, position);
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        });

    }

    @Override
    public int getItemViewType(int position) {
        if(position > 0 && position % 5 == 0)
            return AD_TYPE;

        return CONTENT_TYPE;
    }

    class MediaFileViewHolder extends RecyclerView.ViewHolder {

        // Views
        private ImageView mThumbnailHolder;
        private TextView mTitleHolder, mSizeHolder, mTypeHolder,
                mPathHolder, mDateHolder, mTimeHolder;

        MediaFileViewHolder(@NonNull View itemView) {
            super(itemView);

            mSizeHolder = itemView.findViewById(R.id.size_holder);
            mTypeHolder = itemView.findViewById(R.id.type_holder);
            mPathHolder = itemView.findViewById(R.id.path_holder);
            mDateHolder = itemView.findViewById(R.id.date_holder);
            mTimeHolder = itemView.findViewById(R.id.time_holder);
            mTitleHolder = itemView.findViewById(R.id.title_holder);
            mThumbnailHolder = itemView.findViewById(R.id.thumbnail_holder);
        }
    }
}
