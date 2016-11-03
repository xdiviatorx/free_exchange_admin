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

    String vkGroupId = "-132094733";

    public VkGetOffers() {

    }

    public void init() {
        query(0, 10);
    }

    private void query(int offset, int count) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, vkGroupId);
        params.put(VKApiConst.OFFSET, offset);
        params.put(VKApiConst.COUNT, count);
        params.put("filter", "suggests");
        params.put(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken());
        Log.e(LOG_TAG, "VK ACCESS TOKEN = " + VKAccessToken.currentToken());
        final VKRequest request = VKApi.wall().get(params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.e(LOG_TAG, "RESPONSE = " + response.responseString);
                try {
                    JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                    VkPost[] vkPosts = new VkPost[array.length()];
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
            }
        });
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }
}
