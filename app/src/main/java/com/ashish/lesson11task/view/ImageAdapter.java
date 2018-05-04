package com.ashish.lesson11task.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashish.lesson11task.R;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<File> mFiles;

    private final ListItemClickListener mOnClickListener;

    public ImageAdapter(List<File> files, ListItemClickListener listener) {
        mFiles = files;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(File file);
    }

    public interface ListItemUpdateListener {
        void onListUpdate(File file);
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.image_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ImageViewHolder viewHolder = new ImageViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        holder.listItemImageView.setText(mFiles.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mFiles == null) {
            return 0;
        }
        return mFiles.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            listItemImageView = itemView.findViewById(R.id.tv_item_image_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mFiles.get(clickedPosition));
        }
    }

}
