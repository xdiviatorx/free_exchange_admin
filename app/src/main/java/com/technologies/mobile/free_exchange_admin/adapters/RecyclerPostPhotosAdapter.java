package com.technologies.mobile.free_exchange_admin.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.callbacks.ItemTouchHelperAdapter;
import com.technologies.mobile.free_exchange_admin.callbacks.OnListItemsChangeListener;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diviator on 15.11.2016.
 */

public class RecyclerPostPhotosAdapter extends RecyclerView.Adapter<RecyclerPostPhotosAdapter.MyViewHolder>
    implements ItemTouchHelperAdapter{

    List mData;

    Context mContext;

    boolean mIsChanged = false;

    OnListItemsChangeListener mOnListItemsChangeListener = null;

    public RecyclerPostPhotosAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
    }

    public List getData() {
        return mData;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(getData(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(getData(), i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onMoved() {
        //notifyDataSetChanged();
        setChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;
        ImageView bDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            bDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load( ((VKApiPhoto)mData.get(position)).getPhoto())
                .into(holder.ivPhoto);
        holder.bDelete.setOnClickListener(new OnDeleteButtonClickListener(holder));
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    public void addPhotos(ArrayList<VKApiPhoto> vkApiPhotos){
        for( VKApiPhoto vkApiPhoto : vkApiPhotos){
            addPhoto(vkApiPhoto);
        }
    }

    public void addPhoto(VKApiPhoto vkApiPhoto) {
        mData.add(vkApiPhoto);
        notifyDataSetChanged();
    }

    public void deletePhoto(int position) {
        mData.remove(position);
        notifyDataSetChanged();
        setChanged();
    }

    protected class OnDeleteButtonClickListener implements View.OnClickListener {

        RecyclerView.ViewHolder mHolder;

        public OnDeleteButtonClickListener(RecyclerView.ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public void onClick(View view) {
            deletePhoto(mHolder.getAdapterPosition());
        }
    }

    public void setOnListItemsChangeListener(OnListItemsChangeListener onListItemsChangeListener) {
        this.mOnListItemsChangeListener = onListItemsChangeListener;
    }

    private void setChanged(){
        if( !mIsChanged && mOnListItemsChangeListener != null ){
            mIsChanged = true;
            mOnListItemsChangeListener.onListItemsChanged();
        }
    }
}

