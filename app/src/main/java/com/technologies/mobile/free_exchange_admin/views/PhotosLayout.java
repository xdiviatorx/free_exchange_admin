package com.technologies.mobile.free_exchange_admin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by diviator on 04.11.2016.
 */

public class PhotosLayout extends RelativeLayout {

    private static int MATCH_PARENT = LayoutParams.MATCH_PARENT;
    private static int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;

    public PhotosLayout(Context context) {
        super(context);
    }

    public PhotosLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotosLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addPhoto(String url){
        ImageView photo = new ImageView(getContext());
        LayoutParams layoutParams;
        if( getChildCount() == 0 ){
            initLayoutParams();
            layoutParams = prepareLayoutParams(MATCH_PARENT,MATCH_PARENT);
        }else{

        }
        addView(photo);
    }

    private RelativeLayout.LayoutParams prepareLayoutParams(int width, int height){
        RelativeLayout.LayoutParams mLpWrapWrap = new RelativeLayout.LayoutParams(width, height);

        return mLpWrapWrap;
    }

    private void initLayoutParams(){
        LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
        lp.height = 250;
        setLayoutParams(lp);
    }
}
