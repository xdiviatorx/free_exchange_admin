package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.technologies.mobile.free_exchange_admin.callbacks.VkPhotoUploadedListener;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by diviator on 08.01.2017.
 */

public class VkPhotoUploader {

    private static final String LOG_TAG = "photouploader";

    private VkPhotoUploadedListener vkPhotoUploadedListener = null;

    public VkPhotoUploader(){}

    public void uploadPhotos(String[] urls){
        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(mFragment.getActivity().getContentResolver(), uris.get(i));
        //requests[i] = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), 0, -1 * Integer.parseInt(vkGroupId));
        new MyAsync().execute(urls);
    }

    private class MyAsync extends AsyncTask<String,Void,VKRequest[]>{

        @Override
        protected VKRequest[] doInBackground(String... urls) {
            VKRequest[] requests = new VKRequest[urls.length];
            for( int i = 0; i < urls.length; i++ ){
                Bitmap bitmap = null;
                try {
                    URL url = new URL(urls[i]);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                requests[i] = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap,VKImageParameters.jpgImage(0.9f)),0,-1*Integer.parseInt(VkGroupManager.VK_GROUP_ID));
            }

            return requests;
        }

        @Override
        protected void onPostExecute(VKRequest[] requests) {
            VKBatchRequest request = new VKBatchRequest(requests);
            request.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
                @Override
                public void onComplete(VKResponse[] responses) {
                    super.onComplete(responses);

                    VKApiPhoto[] photos = new VKApiPhoto[responses.length];
                    for (int i = 0; i < responses.length; i++) {
                        photos[i] = ((VKPhotoArray) responses[i].parsedModel).get(0);
                    }

                    if( vkPhotoUploadedListener != null ){
                        vkPhotoUploadedListener.onAttachmentsUploaded(new VKAttachments(photos));
                    }

                    Log.e(LOG_TAG,"SUCCESS" + responses[0].toString());
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    Log.e(LOG_TAG,"ERROR" + error.toString());
                }
            });
        }
    }

    public void setVkPhotoUploadedListener(VkPhotoUploadedListener vkPhotoUploadedListener) {
        this.vkPhotoUploadedListener = vkPhotoUploadedListener;
    }
}
