package com.technologies.mobile.free_exchange_admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.activities.ImagePreviewActivity;
import com.technologies.mobile.free_exchange_admin.callbacks.OnDownloadListener;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;
import com.technologies.mobile.free_exchange_admin.rest.model.GetOffersResponse;
import com.technologies.mobile.free_exchange_admin.rest.model.Offer;
import com.technologies.mobile.free_exchange_admin.views.AutomaticPhotoLayout;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technologies.mobile.free_exchange_admin.activities.ImagePreviewActivity.IMAGES;

/**
 * Created by diviator on 18.08.2016.
 */
public class OffersAdapter extends BaseAdapter {

    public static String LOG_TAG = "mySearchAdapter";

    OnDownloadListener onDownloadListener;

    protected int COUNT = 20;

    protected boolean uploading = false;

    Context mContext;

    ArrayList<Offer> mData;

    int mResource;

    String listType;

    public OffersAdapter(Context context, int resource) {
        this.mContext = context;
        mResource = resource;

        mData = new ArrayList<>();
    }

    private static class ViewHolder {
        TextView tvOffer;
        AutomaticPhotoLayout aplPhotos;
        LinearLayout llContent;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Offer getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return mData.get(i).getId();
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        fitWidthToScreen(viewHolder.llContent);

        String text = getItem(pos).getText();

        viewHolder.tvOffer.setText(text);

        viewHolder.aplPhotos.removeAll();

        if( getItem(pos).getPhotosArray() != null ) {
            for (int i = 0; i < getItem(pos).getPhotosArray().length; i++) {
                viewHolder.aplPhotos.addPhoto(getItem(pos).getPhotosArray()[i]);
            }
        }
        viewHolder.aplPhotos.setOnClickListener(new OnImageClickListener(pos));

        return convertView;
    }

    protected void fitWidthToScreen(LinearLayout linearLayout) {
        Display display = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        lp.width = width;
        linearLayout.setLayoutParams(lp);
    }

    protected class OnImageClickListener implements View.OnClickListener {

        int pos;

        public OnImageClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ImagePreviewActivity.class);
            String[] images = getItem(pos).getPhotosArray();
            intent.putExtra(IMAGES, images);
            mContext.startActivity(intent);
        }
    }

    public void init(String type) {
        //Loader.showProgressBar(context);
        mData = new ArrayList<>();
        uploading = true;
        listType = type;
        downloading(0);
        notifyDataSetChanged();
    }

    public void additionalDownloading(int offset) {
        if (!uploading) {
            uploading = true;
            downloading(offset);
        }
    }

    protected void downloading(int offset) {
        RestClient client = RetrofitService.createService(RestClient.class);
        client.getOffersByStatus(listType, offset, COUNT, RestClient.apiKey).enqueue(new Callback<GetOffersResponse>() {
            @Override
            public void onResponse(Call<GetOffersResponse> call, Response<GetOffersResponse> response) {
                mData.addAll(Arrays.asList(response.body().getOffersArray().getOffers()));
                uploading = false;
                if (Arrays.asList(response.body().getOffersArray().getOffers()).size() == 0) {
                    uploading = true;
                }
                notifyDataSetChanged();
                if( onDownloadListener != null ){
                    onDownloadListener.onDownloaded(response.body().getOffersArray().getCount());
                }
            }

            @Override
            public void onFailure(Call<GetOffersResponse> call, Throwable t) {
                uploading = false;
            }
        });

    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }
}
