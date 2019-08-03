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
import com.example.vic.Model.VideoFile;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;

import java.util.List;

public class VideoFileAdapter extends RecyclerView.Adapter<VideoFileAdapter.VideoFileViewHolder> {

    private Context mContext;
    private List<VideoFile> mVideoFilesList;

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
                    VideoFile file = mVideoFilesList.get(viewHolder.getAdapterPosition());

                    notifyDataSetChanged();
                }
            };


    public VideoFileAdapter(Context mContext, List<VideoFile> mVideoFilesList, ItemClickListener mMediaFileListener) {
        this.mContext = mContext;
        this.mVideoFilesList = mVideoFilesList;
        this.mMediaFileListener = mMediaFileListener;
    }

    @NonNull
    @Override
    public VideoFileAdapter.VideoFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.media_file_item, parent, false);
        return new VideoFileAdapter.VideoFileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFileAdapter.VideoFileViewHolder holder, int position) {
        VideoFile obj = mVideoFilesList.get(position);

        GlideUtils.loadCircularImageAsBitmap(mContext, obj.getmVideoPath(), holder.mThumbnailHolder);

        String size = Constant.SIZE + obj.getmVideoSizeInMB();
        String type = Constant.FILE_TYPE + obj.getmVideoType();
        String path = Constant.PATH + obj.getmVideoPath();

        holder.mSizeHolder.setText(size);
        holder.mTypeHolder.setText(type);
        holder.mPathHolder.setText(path);
        holder.mTitleHolder.setText(obj.getmVideoName());
        holder.mDateHolder.setText(obj.getmVideoCompressionDate());
        holder.mTimeHolder.setText(obj.getmVideoCompressionTime());

        clickOnItem(holder, position);
    }

    // End Point: Trigger Action on RecyclerView item clicked, swipe
    private void clickOnItem(VideoFileViewHolder holder, int position) {
        final VideoFile obj = mVideoFilesList.get(position);

        // When an Item Clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaFileListener.onItemClicked(Constant.VIDEO, obj);
            }
        });

        // When an Item Long Clicked
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mMediaFileListener.onItemLongClicked(Constant.VIDEO, obj);
                return true;
            }
        });

    }

    private void setDataToListener(VideoFile file) {
        mMediaFileListener.onItemSwiped(Constant.IMAGE, file, mSimpleCallback);
    }


    @Override
    public int getItemCount() {
        return mVideoFilesList.size();
    }

    public class VideoFileViewHolder extends RecyclerView.ViewHolder {

        // Views
        private ImageView mThumbnailHolder;
        private TextView mTitleHolder, mSizeHolder, mTypeHolder,
                mPathHolder, mDateHolder, mTimeHolder;

        public VideoFileViewHolder(@NonNull View itemView) {
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
