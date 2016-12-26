package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiChat;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;

/**
 * Created by diviator on 01.12.2016.
 */

public class VkChatFinder implements Runnable {

    public static final String REJECT_CHAT_ID_PREFERENCES = "REJECT_CHAT_ID_PREFERENCES";

    private static final String LOG_TAG = "vkChatFinder";

    static final int COUNT = 200;

    int offset = 0;

    Context mContext;

    public VkChatFinder(Context context){
        mContext = context;
    }

    @Override
    public void run() {
        Log.e(LOG_TAG, "THREAD STARTED...");
        //get all chats via messages.getDialogs
        //lookup chats for title = "Фришоп. Отклоненные новости."
        //save chat_id to sharedPreferences
        offset = 0;
        query();
        Log.e(LOG_TAG, "THREAD STOPPED...");
    }

    private void query() {
        Log.e(LOG_TAG, "QUERY STARTED WITH OFFSET = " + offset);
        VKParameters vkParameters = new VKParameters();
        vkParameters.put(VKApiConst.OFFSET, offset);
        vkParameters.put(VKApiConst.COUNT, COUNT);
        VKRequest request = VKApi.messages().getDialogs(vkParameters);
        request.setModelClass(VKApiGetDialogResponse.class);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.e(LOG_TAG, "RESPONSE COMING WITH OFFSET = " + offset);
                VKApiGetDialogResponse getDialogResponse = (VKApiGetDialogResponse) response.parsedModel;
                Log.e(LOG_TAG, "RESPONSE STRING:\n" + response.responseString);
                if (getDialogResponse.items.size() == 0) {
                    Log.e(LOG_TAG,"EMPTY DIALOG LIST.");
                    return;
                }
                if (!findChatId(getDialogResponse)) {
                    Log.e(LOG_TAG, "DIALOG NOT FOUND");
                    offset += COUNT;
                    query();
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);

            }
        });
    }

    private boolean findChatId(VKApiGetDialogResponse getDialogResponse) {
        for(VKApiDialog dialog : getDialogResponse.items){
            Log.e(LOG_TAG,"CURRENT DIALOG TITLE = " + dialog.message.title);
            if( dialog.message.title.equals(VkGroupManager.ADMIN_REJECT_CHAT_TITLE)){
                save(dialog.message.chat_id);
                return true;
            }
        }
        return false;
    }

    private void save(int chatId){
        Log.e(LOG_TAG,"DIALOG FOUND WITH ID = " + chatId);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putInt(REJECT_CHAT_ID_PREFERENCES,chatId).apply();
    }
}
