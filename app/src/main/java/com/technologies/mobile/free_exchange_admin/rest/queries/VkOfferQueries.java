package com.technologies.mobile.free_exchange_admin.rest.queries;

import com.technologies.mobile.free_exchange_admin.callbacks.OnVkQueryExecuted;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONException;

/**
 * Created by diviator on 02.11.2016.
 */

public class VkOfferQueries {

    private VKApiPost mVkApiPost;

    OnVkQueryExecuted onVkQueryExecuted = null;

    public void setOnVkQueryExecuted(OnVkQueryExecuted onVkQueryExecuted) {
        this.onVkQueryExecuted = onVkQueryExecuted;
    }

    public VkOfferQueries(VKApiPost vkApiPost) {
        mVkApiPost = vkApiPost;
    }

    public void accept() {
        post();
    }

    public void accept(long unixTime) {
        post(unixTime);
    }

    public void reject() {
        delete();
    }

    private void post() {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID, mVkApiPost.getId());
        params.put(VKApiConst.ATTACHMENTS, mVkApiPost.attachments);
        params.put(VKApiConst.SIGNED, 1);
        VKRequest vkRequest = VKApi.wall().post(params);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                int postId = 0;
                try {
                    postId = response.json.getJSONObject("response").getInt("post_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onVkQueryExecuted.onVkQueryExecuted(postId);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    private void post(long unixTime) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID, mVkApiPost.getId());
        params.put(VKApiConst.ATTACHMENTS, mVkApiPost.attachments);
        params.put(VKApiConst.PUBLISH_DATE, unixTime);
        params.put(VKApiConst.SIGNED, 1);
        VKRequest vkRequest = VKApi.wall().post(params);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                int postId = 0;
                try {
                    postId = response.json.getJSONObject("response").getInt("post_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onVkQueryExecuted.onVkQueryExecuted(postId);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    private void delete() {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID, mVkApiPost.getId());
        params.put(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken());
        VKRequest vkRequest = VKApi.wall().delete(params);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    public void createPost(String message, VKAttachments attachments) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.ATTACHMENTS, attachments);
        params.put(VKApiConst.MESSAGE, message);
        params.put(VKApiConst.FROM_GROUP, 1);
        params.put(VKApiConst.SIGNED, 0);
        VKRequest vkRequest = VKApi.wall().post(params);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    public void createPost(String message, VKAttachments attachments, long unixTime) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID, VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.ATTACHMENTS, attachments);
        params.put(VKApiConst.MESSAGE, message);
        params.put(VKApiConst.FROM_GROUP, 1);
        params.put(VKApiConst.SIGNED, 0);
        params.put(VKApiConst.PUBLISH_DATE, unixTime);
        VKRequest vkRequest = VKApi.wall().post(params);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

}
