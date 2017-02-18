package com.technologies.mobile.free_exchange_admin.callbacks;

import com.technologies.mobile.free_exchange_admin.rest.model.VkUser;
import com.vk.sdk.api.model.VKApiPost;

/**
 * Created by diviator on 30.12.2016.
 */

public interface OnUserNameClickListener {

    void onUserNameClicked(VKApiPost vkApiPost, VkUser vkUser);

}
