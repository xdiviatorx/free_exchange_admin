package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.util.Log;
import android.util.LongSparseArray;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.technologies.mobile.free_exchange_admin.callbacks.OnVkDownloadListener;
import com.technologies.mobile.free_exchange_admin.rest.model.VkUser;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKUsersArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        params.put(VKApiConst.EXTENDED,1);
        params.put(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken().accessToken);
        //Log.e(LOG_TAG, "VK ACCESS TOKEN = " + VKAccessToken.currentToken());
        final VKRequest request = VKApi.wall().get(params);
        request.setModelClass(VKPostArray.class);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                downloading = false;

                Log.e(LOG_TAG, response.json.toString());

                int count = 0;
                try {
                    count = response.json.getJSONObject("response").getInt("count");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                VKPostArray vkPostArray = ((VKPostArray) response.parsedModel);
                Map<Long, VkUser> vkUserMap = new HashMap<>();
                try {
                    vkUserMap = readValues(response.json.getJSONObject("response").getJSONArray("profiles"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if( vkUserMap.isEmpty() ) {
                    Log.e(LOG_TAG, "EMPTY MAP");
                }

                if (vkPostArray.size() == 0) {
                    downloading = true;
                }

                if (onVkDownloadListener != null) {
                    onVkDownloadListener.onPostsDownloaded(vkPostArray, vkUserMap, count);
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

    private Map<Long,VkUser> readValues(JSONArray jsonArray) throws JSONException{
        Map<Long, VkUser> res = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();

        Log.e(LOG_TAG,"reading");

        for (int i = 0; i < jsonArray.length(); i++) {
            VkUser vkUser = null;
            try {
                vkUser = mapper.readValue(jsonArray.getJSONObject(i).toString(), typeFactory.constructType(VkUser.class));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,"IOEXCEPTION");
            }
            res.put(vkUser.getId(),vkUser);
            Log.e(LOG_TAG,"OBJECT#"+i);
        }

        return res;
    }

    public void setOnVkDownloadListener(OnVkDownloadListener onVkDownloadListener) {
        this.onVkDownloadListener = onVkDownloadListener;
    }
}
