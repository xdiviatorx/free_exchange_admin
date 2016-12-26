package com.technologies.mobile.free_exchange_admin.activities;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;
import com.technologies.mobile.free_exchange_admin.rest.model.Offer;
import com.technologies.mobile.free_exchange_admin.rest.model.SimpleResponse;
import com.technologies.mobile.free_exchange_admin.views.AutomaticPhotoLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technologies.mobile.free_exchange_admin.fragments.RejectedOffersFragment.REJECT_ACTION;

public class SiteRejectDialogActivity extends OfferDialogActivity {

    protected Offer mOffer;

    @Override
    protected void initPost() {
        mOffer = getIntent().getParcelableExtra(OFFER);

        TextView tvOfferText = (TextView) findViewById(R.id.tvOffer);
        tvOfferText.setText(mOffer.getText());

        AutomaticPhotoLayout aplPhotos = (AutomaticPhotoLayout) findViewById(R.id.aplPhotos);
        if( mOffer.getPhotosArray() != null && mOffer.getPhotosArray().length != 0 && !mOffer.getPhotosArray()[0].isEmpty() ) {
            aplPhotos.addPhoto(mOffer.getPhotosArray()[0]);
        }
    }

    @Override
    protected void query() {
        RestClient client = RetrofitService.createService(RestClient.class);
        client.rejectOffer(mOffer.getId(),0,mEtRejectMessage.getText().toString(),RestClient.apiKey).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Intent intent = new Intent(REJECT_ACTION);
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e(LOG_TAG,t.toString());
            }
        });
        setResult(RESULT_DELETED);
        finish();
    }
}
