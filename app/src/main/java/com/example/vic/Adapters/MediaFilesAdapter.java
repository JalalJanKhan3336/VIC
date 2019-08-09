package com.example.vic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vic.Common.Constant;
import com.example.vic.Listener.ItemClickListener;
import com.example.vic.Model.ImageFile;
import com.example.vic.Model.MediaFiles;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;

import java.util.List;

public class MediaFilesAdapter extends RecyclerView.Adapter<MediaFilesAdapter.MediaFileViewHolder> {


    private Context mContext;
    private List<MediaFiles> mMediaFilesList;

    // Interface Reference
    private ItemClickListener mMediaFileListener;

    // When Item Swiped left or right
    private final ItemTouchHelper.SimpleCallback mSimpleCallback = new
            ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    MediaFiles file = mMediaFilesList.get(viewHolder.getAdapterPosition());

                    setDataToListener(file);

                    notifyDataSetChanged();
                }
            };

    public MediaFilesAdapter(Context mContext, List<MediaFiles> mMediaFilesList, ItemClickListener mMediaFileListener) {
        this.mContext = mContext;
        this.mMediaFilesList = mMediaFilesList;
        this.mMediaFileListener = mMediaFileListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.media_file_item, parent, false);
        return new MediaFileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaFileViewHolder holder, int position) {
        MediaFiles obj = mMediaFilesList.get(position);

        GlideUtils.loadCircularImageAsBitmap(mContext, obj.getmFilePath(), holder.mThumbnailHolder);

        String size = Constant.SIZE + obj.getmFileSizeInMB();
        String type = Constant.FILE_TYPE + obj.getmFileType();
        String path = Constant.PATH + obj.getmFilePath();

        holder.mSizeHolder.setText(size);
        holder.mTypeHolder.setText(type);
        holder.mPathHolder.setText(path);
        holder.mTitleHolder.setText(obj.getmFileName());
        holder.mDateHolder.setText(obj.getmFileCompressionDate());
        holder.mTimeHolder.setText(obj.getmFileCompressionTime());

        clickOnItem(holder, position);
    }

    @Override
    public int getItemCount() {
        return mMediaFilesList.size();
    }

    // End Point: Trigger Action on RecyclerView item clicked, swipe
    private void clickOnItem(MediaFileViewHolder holder, int position) {
        final MediaFiles obj = mMediaFilesList.get(position);

        // When an Item Clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaFileListener.onItemClicked(obj);
            }
        });

        // When an Item Long Clicked
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mMediaFileListener.onItemLongClicked(obj);
                return true;
            }
        });

    }

    private void setDataToListener(MediaFiles file) {
        mMediaFileListener.onItemSwiped(file, mSimpleCallback);
    }

    public class MediaFileViewHolder extends RecyclerView.ViewHolder {

        // Views
        private ImageView mThumbnailHolder;
        private TextView mTitleHolder, mSizeHolder, mTypeHolder,
                mPathHolder, mDateHolder, mTimeHolder;

        public MediaFileViewHolder(@NonNull View itemView) {
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