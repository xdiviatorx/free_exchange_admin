package com.technologies.mobile.free_exchange_admin.callbacks;

import com.vk.sdk.api.model.VKPostArray;

/**
 * Created by diviator on 01.11.2016.
 */

public interface OnVkDownloadListener {

    void onPostsDownloaded(VKPostArray vkPostArray, int count);

}
