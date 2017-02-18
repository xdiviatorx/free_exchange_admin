package com.technologies.mobile.free_exchange_admin.rest.queries;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.technologies.mobile.free_exchange_admin.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by diviator on 30.11.2016.
 */

public class VkMessageSender {

    private Context mContext;

    public VkMessageSender(Context context) {
        mContext = context;
    }

    public void sendRejectMessage(int authorId, String fullName, String postText, String comment) {
        int chatId = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(VkChatFinder.REJECT_CHAT_ID_PREFERENCES, -1);
        if (chatId == -1) {
            Toast.makeText(mContext, R.string.message_to_chat_error, Toast.LENGTH_LONG).show();
        } else {
            sendMessageToChat(createRejectMessage(authorId, fullName, postText, comment), chatId);
        }
    }

    private void sendMessageToChat(String message, int chatId) {
        VKParameters params = new VKParameters();
        params.put(Constants.CHAT_ID, chatId);
        params.put(VKApiConst.MESSAGE, message);
        VKRequest request = new VKRequest("messages.send", params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Toast.makeText(mContext, R.string.message_to_chat_success, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String createRejectMessage(int authorId, String fullName, String postText, String comment) {
        String message = "";

        message += "[id" + authorId + "|" + fullName + "]" + "\n\n";
        message += postText + "\n\n";
        message += comment;

        return message;
    }

    public void sendMessageToUser(long userId, String message){
        sendMessage(userId,message);
    }

    private void sendMessage(long userId, String message) {
        VKParameters params = new VKParameters();
        params.put(VKApiConst.USER_ID, userId);
        params.put(VKApiConst.MESSAGE, message);
        VKRequest request = new VKRequest("messages.send", params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Toast.makeText(mContext, R.string.message_to_user_success, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
