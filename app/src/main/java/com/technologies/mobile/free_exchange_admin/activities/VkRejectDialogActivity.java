package com.technologies.mobile.free_exchange_admin.activities;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;
import com.technologies.mobile.free_exchange_admin.rest.model.SimpleResponse;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkMessageSender;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkOfferQueries;
import com.technologies.mobile.free_exchange_admin.views.AutomaticPhotoLayout;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.model.VKApiPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technologies.mobile.free_exchange_admin.fragments.RejectedOffersFragment.REJECT_ACTION;

public class VkRejectDialogActivity extends OfferDialogActivity{

    private VKApiPost vkApiPost;

    protected void initPost(){
        vkApiPost = getIntent().getParcelableExtra(OFFER);

        TextView tvOfferText = (TextView) findViewById(R.id.tvOffer);
        tvOfferText.setText(vkApiPost.text);

        AutomaticPhotoLayout aplPhotos = (AutomaticPhotoLayout) findViewById(R.id.aplPhotos);
        if( vkApiPost.photos.size() != 0 ) {
            aplPhotos.addPhoto(vkApiPost.photos.get(0));
        }
    }

    protected void query(){
        VkMessageSender vkMessageSender = new VkMessageSender(this);
        vkMessageSender.sendRejectMessage(vkApiPost.from_id,vkApiPost.text,mEtRejectMessage.getText().toString());

        RestClient client = RetrofitService.createService(RestClient.class);
        Log.e(LOG_TAG,"ID = " + vkApiPost.getId()+"");
        client.rejectOfferVk(vkApiPost.getId(),0,mEtRejectMessage.getText().toString(), VKAccessToken.currentToken().accessToken, RestClient.apiKey).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                VkOfferQueries vkOfferQueries = new VkOfferQueries(vkApiPost);
                vkOfferQueries.reject();
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
