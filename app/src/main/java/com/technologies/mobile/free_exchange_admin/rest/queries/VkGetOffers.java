package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.util.Log;

import com.technologies.mobile.free_exchange_admin.callbacks.OnDownloadListener;
import com.technologies.mobile.free_exchange_admin.rest.model.VkPost;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKPostArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by diviator on 01.11.2016.
 */

public class VkGetOffers {

    private static String LOG_TAG = "VkGetOffers";

    OnDownloadListener onDownloadListener;

    boolean downloading = false;

    public VkGetOffers() {

    }

    public void init() {
        downloading = true;
        query(0);
    }

    public void additionalDownloading(int offset){
        if( !downloading ){
            downloading = true;
            query(offset);
        }
    }

    private void query(int offset) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.OFFSET, offset);
        params.put(VKApiConst.COUNT, Constants.DOWNLOADING_LENGTH);
        params.put(Constants.FILTER, Constants.SUGGESTS);
        params.put(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken());
        Log.e(LOG_TAG, "VK ACCESS TOKEN = " + VKAccessToken.currentToken());
        final VKRequest request = VKApi.wall().get(params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                downloading = false;
                Log.e(LOG_TAG, "RESPONSE = " + response.responseString);
                try {
                    JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                    VkPost[] vkPosts = new VkPost[array.length()];
                    if( array.length() == 0 ){
                        downloading = true;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        vkPosts[i] = new VkPost(array.getJSONObject(i));
                    }

                    if (onDownloadListener != null) {
                        onDownloadListener.onPostsDownloaded(vkPosts);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e(LOG_TAG, "ERROR = " + error.errorMessage);
                downloading = false;
            }
        });
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }
}
