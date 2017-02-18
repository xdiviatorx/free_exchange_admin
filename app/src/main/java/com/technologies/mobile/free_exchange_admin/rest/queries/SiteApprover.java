package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.util.Log;

import com.technologies.mobile.free_exchange_admin.callbacks.VkPhotoUploadedListener;
import com.technologies.mobile.free_exchange_admin.logic.VkPhotoConverter;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;
import com.technologies.mobile.free_exchange_admin.rest.model.CheckResponse;
import com.technologies.mobile.free_exchange_admin.rest.model.Offer;
import com.technologies.mobile.free_exchange_admin.rest.model.SimpleResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diviator on 26.11.2016.
 */

public class SiteApprover extends Approver {

    private static final String LOG_TAG = "siteApprover";

    Offer mOffer;
    List<VKApiPhoto> mVkApiPhotos;
    String mMessage;

    public SiteApprover(Offer offer, String message, List<VKApiPhoto> vkApiPhotos){
        mOffer = offer;
        mMessage = message;
        mVkApiPhotos = vkApiPhotos;
    }

    @Override
    protected void check() {
        RestClient client = RetrofitService.createService(RestClient.class);
        client.checkOffer(mOffer.getId(),RestClient.apiKey).enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                showApproveDialog(response.body().getPublish().getCheck());
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void editAndCheck() {
        RestClient client = RetrofitService.createService(RestClient.class);
        JSONArray photos = null;
        try {
            photos = VkPhotoConverter.getPhotoObjectsFromVkPhotos(mVkApiPhotos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.editAndCheckOffer(mOffer.getId(),mMessage,photos,RestClient.apiKey).enqueue(new Callback<CheckResponse>() {
            @Override
            public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                showApproveDialog(response.body().getPublish().getCheck());
            }

            @Override
            public void onFailure(Call<CheckResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void publishVk() {
        VkPhotoUploader uploader = new VkPhotoUploader();
        uploader.setVkPhotoUploadedListener(new VkPhotoUploadedListener() {
            @Override
            public void onAttachmentsUploaded(VKAttachments attachments) {
                VkOfferQueries vkOfferQueries = new VkOfferQueries(null);
                //VKAttachments attachments = new VKAttachments(mVkApiPhotos);
                vkOfferQueries.createPost(createMessage(),attachments);
            }
        });
        if( mOffer.getPhotosArray() != null ) {
            uploader.uploadPhotos(mOffer.getPhotosArray());
        }
    }

    @Override
    protected void publishVk(final long unixTime) {
        VkPhotoUploader uploader = new VkPhotoUploader();
        uploader.setVkPhotoUploadedListener(new VkPhotoUploadedListener() {
            @Override
            public void onAttachmentsUploaded(VKAttachments attachments) {
                VkOfferQueries vkOfferQueries = new VkOfferQueries(null);
                //VKAttachments attachments = new VKAttachments(mVkApiPhotos);
                vkOfferQueries.createPost(createMessage(),attachments,unixTime);
            }
        });
        if( mOffer.getPhotosArray() != null ) {
            uploader.uploadPhotos(mOffer.getPhotosArray());
        }
    }

    private String createMessage(){
        String message = mMessage;
        message += "\n\n";
        if( mOffer.getUserData() != null && mOffer.getUserData().getVkId() != null && mOffer.getUserData().getName() != null) {
            message += "[id" + mOffer.getUserData().getVkId() + "|" + mOffer.getUserData().getName() + "]";
        }
        return message;
    }

    @Override
    protected void siteSynchronization() {
        RestClient client = RetrofitService.createService(RestClient.class);
        client.approveOffer(mOffer.getId(),createPublishTo(),RestClient.apiKey).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void noPublishing() {

    }
}
