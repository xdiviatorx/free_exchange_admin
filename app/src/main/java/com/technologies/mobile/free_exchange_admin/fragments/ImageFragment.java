package com.technologies.mobile.free_exchange_admin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.activities.ImagePreviewActivity;

/**
 * Created by diviator on 07.11.2016.
 */

public class ImageFragment extends Fragment {

    private String imageUrl;

    public static ImageFragment getInstance(String url){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ImagePreviewActivity.IMAGE,url);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments().getString(ImagePreviewActivity.IMAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(),R.layout.image_fragment,null);

        if( view != null ) {
            ImageView iv = (ImageView) view.findViewById(R.id.image);

            if (!imageUrl.isEmpty()) {
                Picasso.with(getContext())
                        .load(imageUrl)
                        .into(iv);
            } else {
                iv.setImageResource(R.drawable.no_photo_big);
            }
        }

        return view;
    }
}
