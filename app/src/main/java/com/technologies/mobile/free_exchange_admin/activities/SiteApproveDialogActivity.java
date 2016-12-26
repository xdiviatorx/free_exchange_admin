package com.technologies.mobile.free_exchange_admin.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;
import com.technologies.mobile.free_exchange_admin.rest.model.SimpleResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technologies.mobile.free_exchange_admin.activities.EditOfferVkActivity.RESULT_APPROVED;
import static com.technologies.mobile.free_exchange_admin.fragments.ApprovedOffersFragment.APPROVE_ACTION;

public class SiteApproveDialogActivity extends SiteRejectDialogActivity {

    @Override
    protected void initViews() {
        super.initViews();
        mEtRejectMessage.setVisibility(View.GONE);
        findViewById(R.id.tvMessageText).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tvIsAction)).setText(R.string.is_approve);
    }

    @Override
    protected void query() {
        RestClient client = RetrofitService.createService(RestClient.class);
        client.approveOffer(mOffer.getId(),null, RestClient.apiKey).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Intent intent = new Intent(APPROVE_ACTION);
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e(LOG_TAG,"siteapproveerror = " + t.toString());
            }
        });
        setResult(RESULT_APPROVED);
        finish();
    }
}
