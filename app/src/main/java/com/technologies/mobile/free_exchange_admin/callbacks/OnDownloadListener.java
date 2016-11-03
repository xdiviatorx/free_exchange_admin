package com.technologies.mobile.free_exchange_admin.callbacks;

import com.technologies.mobile.free_exchange_admin.rest.model.VkPost;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKPostArray;

/**
 * Created by diviator on 01.11.2016.
 */

public interface OnDownloadListener {

    void onPostsDownloaded(VkPost[] vkPosts);

}
