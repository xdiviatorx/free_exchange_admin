package com.technologies.mobile.free_exchange_admin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange_admin.R;

/**
 * Created by diviator on 07.11.2016.
 */

public class AutomaticPhotoLayout extends TableLayout {

    private static final String LOG_TAG = "AutomaticPhotoLayout";

    public static final int FIRST_COLUMN_INDEX = 0;
    public static final int SECOND_COLUMN_INDEX = 1;
    public static final int THIRD_COLUMN_INDEX = 2;

    int photos = 0;

    public AutomaticPhotoLayout(Context context) {
        super(context);
    }

    public AutomaticPhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addPhoto(String url) {
        TableRow row;
        int height;
        if (photos < 3) {
            initRow(FIRST_COLUMN_INDEX);
            row = (TableRow) getChildAt(FIRST_COLUMN_INDEX);
            height = mGetTableHeight(FIRST_COLUMN_INDEX);
        } else if (photos < 5) {
            initRow(SECOND_COLUMN_INDEX);
            row = (TableRow) getChildAt(SECOND_COLUMN_INDEX);
            height = mGetTableHeight(SECOND_COLUMN_INDEX);
        } else {
            initRow(THIRD_COLUMN_INDEX);
            row = (TableRow) getChildAt(THIRD_COLUMN_INDEX);
            height = mGetTableHeight(THIRD_COLUMN_INDEX);
        }
        row.addView(getImage(url,height));

        photos++;
    }

    public void removeAll(){
        removeAllViews();
        photos=0;
    }

    private void initRow(int index) {
        int count = getChildCount();
        if (count <= index) {
            TableRow tableRow = getTableRow(mGetTableHeight(index));
            addView(tableRow);
            Log.e(LOG_TAG,"row added");
        }
    }

    private TableRow getTableRow(int height) {
        TableRow tableRow = new TableRow(getContext());
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        tableRow.setLayoutParams(lp);

        return tableRow;
    }

    private int mGetTableHeight(int index) {
        switch (index) {
            case FIRST_COLUMN_INDEX: {
                return 200;
            }
            case SECOND_COLUMN_INDEX: {
                return 150;
            }
            case THIRD_COLUMN_INDEX: {
                return 70;
            }
            default:{
                return 0;
            }
        }
    }

    private ImageView getImage(String url, int height){
        ImageView imageView = new ImageView(getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,height,1);
        lp.setMargins(3,3,3,3);
        imageView.setLayoutParams(lp);
        imageView.setMinimumWidth(1000);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(getContext()).load(url).into(imageView);

        Log.e(LOG_TAG,"image added");

        return imageView;
    }
}
