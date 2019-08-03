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
import com.example.vic.Model.AllMediaFiles;
import com.example.vic.Model.ImageFile;
import com.example.vic.R;
import com.example.vic.Utils.GlideUtils;

import java.util.List;

public class ImageFileAdapter extends RecyclerView.Adapter<ImageFileAdapter.ImageFileViewHolder> {

    private Context mContext;
    private List<ImageFile> mImageFilesList;

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
                    ImageFile file = mImageFilesList.get(viewHolder.getAdapterPosition());

                    setDataToListener(file);

                    notifyDataSetChanged();
                }
            };

    public ImageFileAdapter(Context mContext, List<ImageFile> mImageFilesList, ItemClickListener mMediaFileListener) {
        this.mContext = mContext;
        this.mImageFilesList = mImageFilesList;
        this.mMediaFileListener = mMediaFileListener;
    }

    @NonNull
    @Override
    public ImageFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.media_file_item, parent, false);
        return new ImageFileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFileViewHolder holder, int position) {
        ImageFile obj = mImageFilesList.get(position);

        GlideUtils.loadCircularImageAsBitmap(mContext, obj.getmImagePath(), holder.mThumbnailHolder);

        String size = Constant.SIZE + obj.getmImageSizeInMB();
        String type = Constant.FILE_TYPE + obj.getmImageType();
        String path = Constant.PATH + obj.getmImagePath();

        holder.mSizeHolder.setText(size);
        holder.mTypeHolder.setText(type);
        holder.mPathHolder.setText(path);
        holder.mTitleHolder.setText(obj.getmImageName());
        holder.mDateHolder.setText(obj.getmImageCompressionDate());
        holder.mTimeHolder.setText(obj.getmImageCompressionTime());

        clickOnItem(holder, position);
    }

    @Override
    public int getItemCount() {
        return mImageFilesList.size();
    }

    // End Point: Trigger Action on RecyclerView item clicked, swipe
    private void clickOnItem(ImageFileViewHolder holder, int position) {
        final ImageFile obj = mImageFilesList.get(position);

        // When an Item Clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaFileListener.onItemClicked(Constant.IMAGE, obj);
            }
        });

        // When an Item Long Clicked
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mMediaFileListener.onItemLongClicked(Constant.IMAGE, obj);
                return true;
            }
        });

    }

    private void setDataToListener(ImageFile file) {
        mMediaFileListener.onItemSwiped(Constant.IMAGE, file, mSimpleCallback);
    }

    public class ImageFileViewHolder extends RecyclerView.ViewHolder {

        // Views
        private ImageView mThumbnailHolder;
        private TextView mTitleHolder, mSizeHolder, mTypeHolder,
                         mPathHolder, mDateHolder, mTimeHolder;

        public ImageFileViewHolder(@NonNull View itemView) {
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
