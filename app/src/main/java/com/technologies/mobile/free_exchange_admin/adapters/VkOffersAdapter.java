package com.technologies.mobile.free_exchange_admin.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.callbacks.OnButtonClickCallback;
import com.technologies.mobile.free_exchange_admin.callbacks.OnImageSetClickCallback;
import com.technologies.mobile.free_exchange_admin.views.AutomaticPhotoLayout;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPostArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by diviator on 01.11.2016.
 */

public class VkOffersAdapter extends BaseAdapter {

    private String LOG_TAG = "VkOffersAdapter";

    private OnButtonClickCallback onButtonClickCallback;

    private OnImageSetClickCallback onImageSetClickCallback;

    private Context mContext;
    private int mResource;

    private ArrayList<VKApiPost> mData;

    public VkOffersAdapter(Context context, int resource) {
        mContext = context;
        mResource = resource;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public VKApiPost getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mData.get(i).getId();
    }

    private class ViewHolder {
        LinearLayout llContent;
        TextView tvOffer;
        AutomaticPhotoLayout aplPhotos;
        TextView tvDate;
        Button bReject;
        Button bAccept;

    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(mResource, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvOffer = (TextView) view.findViewById(R.id.tvOffer);
            viewHolder.aplPhotos = (AutomaticPhotoLayout) view.findViewById(R.id.aplPhotos);
            viewHolder.bAccept = (Button) view.findViewById(R.id.bAccept);
            viewHolder.bReject = (Button) view.findViewById(R.id.bReject);
            viewHolder.llContent = (LinearLayout) view.findViewById(R.id.llContent);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            view.setTag(viewHolder);
        }

        fitWidthToScreen(viewHolder.llContent);

        viewHolder.tvOffer.setText(getItem(pos).text);
        viewHolder.aplPhotos.removeAll();

        ArrayList<String> photos = getItem(pos).photos;
        viewHolder.aplPhotos.addPhotos(photos);

        viewHolder.bAccept.setOnClickListener(new OnButtonClickListener(pos));
        viewHolder.bReject.setOnClickListener(new OnButtonClickListener(pos));

        viewHolder.aplPhotos.setOnClickListener(new OnImageSetClickListener(photos.toArray(new String[photos.size()])));


        viewHolder.tvDate.setText(getItem(pos).dateString);

        return view;
    }

    private void fitWidthToScreen(LinearLayout linearLayout) {
        Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        lp.width = width;
        linearLayout.setLayoutParams(lp);
    }

    public void addPost(VKPostArray vkPostArray) {
        for( int i = 0; i < vkPostArray.size(); i++ ){
            mData.add(vkPostArray.get(i));
        }
    }

    public void deletePost(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    public void clear(){
        mData = new ArrayList<>();
        notifyDataSetChanged();
    }

    private class OnButtonClickListener implements View.OnClickListener {

        private int pos;

        public OnButtonClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bAccept: {
                    if (onButtonClickCallback != null) {
                        onButtonClickCallback.onButtonClick(OnButtonClickCallback.ButtonType.ACCEPT, pos);
                    }
                    break;
                }
                case R.id.bReject: {
                    if (onButtonClickCallback != null) {
                        onButtonClickCallback.onButtonClick(OnButtonClickCallback.ButtonType.REJECT, pos);
                    }
                    break;
                }
            }
        }
    }

    public void setOnButtonClickCallback(OnButtonClickCallback onButtonClickCallback) {
        this.onButtonClickCallback = onButtonClickCallback;
    }

    private class OnImageSetClickListener implements View.OnClickListener {

        String[] urls;

        public OnImageSetClickListener(String[] urls) {
            this.urls = urls;
        }

        @Override
        public void onClick(View view) {
            if (onImageSetClickCallback != null) {
                onImageSetClickCallback.onImageSetClick(urls);
                for( String str : urls ) {
                    Log.e(LOG_TAG, str);
                }
            }
        }
    }

    public void setOnImageSetClickCallback(OnImageSetClickCallback onImageSetClickCallback) {
        this.onImageSetClickCallback = onImageSetClickCallback;
    }
}
