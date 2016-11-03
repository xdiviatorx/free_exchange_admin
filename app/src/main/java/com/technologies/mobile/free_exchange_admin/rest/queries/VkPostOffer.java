package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.widget.Toast;

import com.technologies.mobile.free_exchange_admin.rest.model.VkPost;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by diviator on 02.11.2016.
 */

public class VkPostOffer {

    private VkPost mVkPost;

    public VkPostOffer(VkPost vkPost){
        mVkPost = vkPost;
    }

    public void accept(long postId){
        post(postId);
    }

    public void reject(long postId){
        delete(postId);
    }

    private void post(long postId){
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID,VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID,postId);
        params.put(VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken());
        params.put(VKApiConst.ATTACHMENTS, mVkPost.getAttachments());
        params.put(VKApiConst.SIGNED,1);
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

    private void delete(long postId){
        VKParameters params = new VKParameters();
        params.put(VKApiConst.OWNER_ID,VkGroupManager.VK_GROUP_ID);
        params.put(VKApiConst.POST_ID,postId);
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

}
