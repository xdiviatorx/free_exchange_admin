package com.technologies.mobile.free_exchange_admin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

/**
 * Created by diviator on 01.11.2016.
 */

public class AttachmentsLayout extends HorizontalScrollView {

    private static String LOG_TAG = "attachmentsLayout";

    private LinearLayout mLinearLayout;

    public AttachmentsLayout(Context context) {
        super(context);
        init();
    }

    public AttachmentsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttachmentsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mLinearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(layoutParams);
        addView(mLinearLayout);
    }

    public void removeAll(){
        mLinearLayout.removeAllViews();
    }


    public void addPhoto(String url, int index){
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,150);
        layoutParams.setMargins(5,5,5,5);
        imageView.setLayoutParams(layoutParams);

        Glide.with(getContext()).load(url).into(imageView);
        //Picasso.with(getContext()).load(url).into(imageView);

        mLinearLayout.addView(imageView,index);
        Log.e(LOG_TAG,"IMAGE ADDED");
    }

}
