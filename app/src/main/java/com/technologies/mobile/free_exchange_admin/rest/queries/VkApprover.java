package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.util.Log;

import com.bumptech.glide.request.Request;
import com.technologies.mobile.free_exchange_admin.callbacks.OnVkQueryExecuted;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;
import com.technologies.mobile.free_exchange_admin.rest.model.CheckResponse;
import com.technologies.mobile.free_exchange_admin.rest.model.SimpleResponse;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 26.11.2016.
 */

public class VkApprover extends Approver implements OnVkQueryExecuted {

    private static String LOG_TAG = "vkApprover";

    VKApiPost mVkApiPost;
    String mMessage;
    List<VKApiPhoto> mVkApiPhotos;
    int mCatId;

    int newPostId = -1;

    public VkApprover(VKApiPost vkApiPost, String message, List<VKApiPhoto> vkApiPhotos, int catId) {
        mVkApiPost = vkApiPost;
        mMessage = message;
        mVkApiPhotos = vkApiPhotos;
        mCatId = catId;
    }

    @Override
    protected void check() {
        RestClient restClient = RetrofitService.createService(RestClient.class);
        Log.e(LOG_TAG, "POST ID = " + mVkApiPost.getId());
        restClient.checkOfferVk(mVkApiPost.getId(), RestClient.apiKey).enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                showApproveDialog(response.body().getPublish().getCheck());
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {
                Log.e(LOG_TAG, "ERROR CHECK " + t.toString());
            }
        });
    }

    @Override
    protected void editAndCheck() {
        publish();
    }

    private void publish() {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID, mVkApiPost.id);
        params.put(VKApiConst.MESSAGE, createVkMessage());
        params.put(VKApiConst.ATTACHMENTS, new VKAttachments(mVkApiPhotos));
        params.put(VKApiConst.PUBLISH_DATE, System.currentTimeMillis() / 1000 + 30 * 24 * 60 * 60);
        VKApi.wall().post(params).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                check();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e(LOG_TAG, "PUBLISH " + error.toString());
            }
        });
    }

    private void edit() {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID, mVkApiPost.id);
        params.put(VKApiConst.MESSAGE, createVkMessage());
        VKAttachments attachments = new VKAttachments(mVkApiPhotos);
        params.put(VKApiConst.ATTACHMENTS, attachments);
        params.put(VKApiConst.PUBLISH_DATE, 2000000000);
        VKRequest request = VKApi.wall().edit(params);
        request.addExtraParameter(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken().accessToken);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                check();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e(LOG_TAG, "EDIT " + error.toString());
            }
        });
    }

    private String createVkMessage(){
        return mMessage + "\n\n" + createVkLinks();
    }

    @Override
    public void publish(boolean vk, boolean site, boolean telegramm) {
        //super.publish(vk, site, telegramm);
        this.vk = vk;
        this.site = site;
        this.telegramm = telegramm;

        if (vk) {
            publishVk();
        } else {
            siteSynchronization();
        }

        //siteSynchronization();

        if (!vk && !site) {
            noPublishing();
        }
    }

    @Override
    public void publish(boolean vk, long unixTime, boolean site, boolean telegramm) {
        //super.publish(vk, unixTime, site, telegramm);
        this.vk = vk;
        this.site = site;
        this.telegramm = telegramm;

        if (vk) {
            publishVk(unixTime);
        } else {
            siteSynchronization();
        }

        //siteSynchronization();

        if (!vk && !site) {
            noPublishing();
        }
    }

    boolean isVkPublished = false;

    @Override
    protected void publishVk() {
        VkOfferQueries vkOfferQueries = new VkOfferQueries(mVkApiPost);
        vkOfferQueries.setOnVkQueryExecuted(this);
        vkOfferQueries.accept();
        isVkPublished = true;
    }

    @Override
    protected void publishVk(long unixTime) {
        VkOfferQueries vkOfferQueries = new VkOfferQueries(mVkApiPost);
        vkOfferQueries.setOnVkQueryExecuted(this);
        vkOfferQueries.accept(unixTime);
        isVkPublished = true;
    }

    @Override
    protected void siteSynchronization() {
        Log.e(LOG_TAG, "ID VK POST = " + mVkApiPost.getId());
        Log.e(LOG_TAG, "TYPE = " + createPublishTo().toString());
        RestClient client = RetrofitService.createService(RestClient.class);
        int id;
        if (newPostId != -1) {
            id = newPostId;
        } else {
            id = mVkApiPost.getId();
        }
        client.approveOfferVk(id, createPublishTo(), mCatId, RestClient.apiKey).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (!isVkPublished) {
                    VkOfferQueries vkOfferQueries = new VkOfferQueries(mVkApiPost);
                    vkOfferQueries.reject();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e(LOG_TAG, "ERROR = " + t.toString());
            }
        });
    }

    @Override
    protected void noPublishing() {
        VkOfferQueries vkOfferQueries = new VkOfferQueries(mVkApiPost);
        vkOfferQueries.reject();
    }

    @Override
    public void onVkQueryExecuted(int postId) {
        newPostId = postId;
        siteSynchronization();
    }
}
