package com.technologies.mobile.free_exchange_admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.callbacks.OnButtonClickCallback;
import com.technologies.mobile.free_exchange_admin.views.AutomaticPhotoLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by diviator on 19.11.2016.
 */

public class SiteOffersAdapter extends OffersAdapter {

    OnButtonClickCallback mOnButtonClickCallback = null;

    public SiteOffersAdapter(Context context, int resource) {
        super(context, resource);
    }

    private static class ViewHolder {
        TextView tvOffer;
        AutomaticPhotoLayout aplPhotos;
        LinearLayout llContent;
        TextView tvDate;
        Button bAccept;
        Button bReject;
        TextView tvName;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvOffer = (TextView) convertView.findViewById(R.id.tvOffer);
            viewHolder.aplPhotos = (AutomaticPhotoLayout) convertView.findViewById(R.id.aplPhotos);
            viewHolder.llContent = (LinearLayout) convertView.findViewById(R.id.llContent);
            viewHolder.bAccept = (Button) convertView.findViewById(R.id.bAccept);
            viewHolder.bReject = (Button) convertView.findViewById(R.id.bReject);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        fitWidthToScreen(viewHolder.llContent);

        String text = getItem(pos).getText();

        viewHolder.tvOffer.setText(text);

        viewHolder.aplPhotos.removeAll();

        if( getItem(pos).getPhotosArray() != null) {
            for (int i = 0; i < getItem(pos).getPhotosArray().length; i++) {
                viewHolder.aplPhotos.addPhoto(getItem(pos).getPhotosArray()[i]);
            }
        }

        viewHolder.aplPhotos.setOnClickListener(new OnImageClickListener(pos));

        viewHolder.tvDate.setText(getItem(pos).getDateString());

        viewHolder.bAccept.setOnClickListener(new OnButtonClickListener(pos));
        viewHolder.bReject.setOnClickListener(new OnButtonClickListener(pos));

        if( getItem(pos).getUserData() != null && getItem(pos).getUserData().getName() != null ) {
            viewHolder.tvName.setText(getItem(pos).getUserData().getName());
            viewHolder.tvName.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        }else{
            viewHolder.tvName.setText(R.string.author_not_identify);
            viewHolder.tvName.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
        }

        return convertView;
    }

    public void deleteOffer(int pos){
        mData.remove(pos);
        notifyDataSetChanged();
    }

    private class OnButtonClickListener implements View.OnClickListener {

        private int pos;

        OnButtonClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bAccept: {
                    if (mOnButtonClickCallback != null) {
                        mOnButtonClickCallback.onButtonClick(OnButtonClickCallback.ButtonType.ACCEPT, pos);
                    }
                    break;
                }
                case R.id.bReject: {
                    if (mOnButtonClickCallback != null) {
                        mOnButtonClickCallback.onButtonClick(OnButtonClickCallback.ButtonType.REJECT, pos);
                    }
                    break;
                }
            }
        }
    }

    public void setOnButtonClickCallback(OnButtonClickCallback onButtonClickCallback) {
        mOnButtonClickCallback = onButtonClickCallback;
    }
}
