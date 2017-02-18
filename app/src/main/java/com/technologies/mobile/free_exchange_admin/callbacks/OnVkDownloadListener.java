package com.technologies.mobile.free_exchange_admin.callbacks;

import com.technologies.mobile.free_exchange_admin.rest.model.VkUser;
import com.vk.sdk.api.model.VKPostArray;

import java.util.Map;

/**
 * Created by diviator on 01.11.2016.
 */

public interface OnVkDownloadListener {

    void onPostsDownloaded(VKPostArray vkPostArray, Map<Long,VkUser> profiles, int count);

}
