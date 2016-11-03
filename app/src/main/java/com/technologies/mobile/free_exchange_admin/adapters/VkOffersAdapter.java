package com.technologies.mobile.free_exchange_admin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.callbacks.OnButtonClickCallback;
import com.technologies.mobile.free_exchange_admin.rest.model.VkPost;
import com.technologies.mobile.free_exchange_admin.views.AttachmentsLayout;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKWallPostResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by diviator on 01.11.2016.
 */

public class VkOffersAdapter extends BaseAdapter {

    private String LOG_TAG = "VkOffersAdapter";

    private OnButtonClickCallback onButtonClickCallback;

    private Context mContext;
    private int mResource;

    private ArrayList<VkPost> mData;

    public VkOffersAdapter(Context context, int resource){
        mContext = context;
        mResource = resource;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public VkPost getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mData.get(i).getId();
    }

    private class ViewHolder{
        TextView tvOffer;
        AttachmentsLayout alPhotos;
        Button bReject;
        Button bAccept;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if( view != null ){
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(mContext).inflate(mResource,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tvOffer = (TextView) view.findViewById(R.id.tvOffer);
            viewHolder.alPhotos = (AttachmentsLayout) view.findViewById(R.id.alPhotos);
            viewHolder.bAccept = (Button) view.findViewById(R.id.bAccept);
            viewHolder.bReject = (Button) view.findViewById(R.id.bReject);
            view.setTag(viewHolder);
        }

        viewHolder.tvOffer.setText(getItem(pos).getText());
        ArrayList<String> photos = getItem(pos).getPhoto130Array();
        viewHolder.alPhotos.removeAll();
        for( int i = 0; i < photos.size(); i++) {
            Log.e(LOG_TAG,"Photo " + pos + " " + i + " = " + photos.get(i));
            viewHolder.alPhotos.addPhoto(photos.get(i),i);
        }

        viewHolder.bAccept.setOnClickListener(new OnButtonClickListener(pos));
        viewHolder.bReject.setOnClickListener(new OnButtonClickListener(pos));

        return view;
    }

    public void addPost(VkPost[] vkPosts){
        mData.addAll(Arrays.asList(vkPosts));
    }

    public void deletePost(int pos){
        mData.remove(pos);
    }

    private class OnButtonClickListener implements View.OnClickListener{

        private int pos;

        public OnButtonClickListener(int pos){
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bAccept:{
                    if( onButtonClickCallback != null){
                        onButtonClickCallback.onButtonClick(OnButtonClickCallback.ButtonType.ACCEPT,pos);
                    }
                    break;
                }
                case R.id.bReject:{
                    if( onButtonClickCallback != null){
                        onButtonClickCallback.onButtonClick(OnButtonClickCallback.ButtonType.REJECT,pos);
                    }
                    break;
                }
            }
        }
    }

    public void setOnButtonClickCallback(OnButtonClickCallback onButtonClickCallback) {
        this.onButtonClickCallback = onButtonClickCallback;
    }
}
