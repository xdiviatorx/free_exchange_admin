package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.util.Log;

import com.technologies.mobile.free_exchange_admin.callbacks.OnVkDownloadListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKPostArray;

import org.json.JSONException;

/**
 * Created by diviator on 01.11.2016.
 */

public class VkGetOffers {

    private static String LOG_TAG = "VkGetOffers";

    OnVkDownloadListener onVkDownloadListener;

    boolean downloading = false;

    public VkGetOffers() {

    }

    public void init() {
        downloading = true;
        query(0);
    }

    public void additionalDownloading(int offset) {
        if (!downloading) {
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
        params.put(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken().accessToken);
        //Log.e(LOG_TAG, "VK ACCESS TOKEN = " + VKAccessToken.currentToken());
        final VKRequest request = VKApi.wall().get(params);
        request.setModelClass(VKPostArray.class);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                downloading = false;

                Log.e(LOG_TAG,response.json.toString());

                int count = 0;
                try {
                    count = response.json.getJSONObject("response").getInt("count");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                VKPostArray vkPostArray = ((VKPostArray) response.parsedModel);

                if (vkPostArray.size() == 0) {
                    downloading = true;
                }

                if (onVkDownloadListener != null) {
                    onVkDownloadListener.onPostsDownloaded(vkPostArray,count);
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

    public void setOnVkDownloadListener(OnVkDownloadListener onVkDownloadListener) {
        this.onVkDownloadListener = onVkDownloadListener;
    }
}
