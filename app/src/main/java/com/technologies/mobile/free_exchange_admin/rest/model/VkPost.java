package com.technologies.mobile.free_exchange_admin.rest.model;

import android.util.Log;

import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by diviator on 02.11.2016.
 */

public class VkPost {
/*
    {"response":
        {"count":3,
                "items":
                [{
                "id":6,
                "from_id":86784711,
                "owner_id":-132094733,
                "date":1478033716,
                "marked_as_ads":0,
                "post_type":"suggest",
                "text":"Предложение 3",
                "can_publish":1,
                "can_delete":1,
                "attachments":[{
                    "type":"photo",
                    "photo":{
                    "id":439659936,
                    "album_id":-8,
                    "owner_id":-132094733,
                    "user_id":86784711,
                    "photo_75":"https:\/\/pp.vk.me\/c626216\/v626216711\/36394\/EVEG7vtKJHo.jpg",
                    "photo_130":"https:\/\/pp.vk.me\/c626216\/v626216711\/36395\/p5xi2Oy4Zwk.jpg",
                    "photo_604":"https:\/\/pp.vk.me\/c626216\/v626216711\/36396\/MCL967Oq-pY.jpg",
                    "photo_807":"https:\/\/pp.vk.me\/c626216\/v626216711\/36397\/V2efV3VnwKE.jpg",
                    "photo_1280":"https:\/\/pp.vk.me\/c626216\/v626216711\/36398\/EicBGUW0sOg.jpg",
                    "width":1280,
                    "height":768,
                    "text":"",
                    "date":1478033715,
                    "access_key":"dfff08f052b95b226d"}},
                    {"type":"photo","photo":{"id":439659937,"album_id":-8,"owner_id":-132094733,"user_id":86784711,"photo_75":"https:\/\/pp.vk.me\/c626216\/v626216711\/3639d\/wqW43D8-Nas.jpg","photo_130":"https:\/\/pp.vk.me\/c626216\/v626216711\/3639e\/I0c5qXhipeA.jpg","photo_604":"https:\/\/pp.vk.me\/c626216\/v626216711\/3639f\/TkkZ35GjuVc.jpg","width":500,"height":500,"text":"","date":1478033715,"access_key":"b127b1d7bcd980b9c6"}}],"post_source":{"type":"api","platform":"android"},"comments":{"count":0,"can_post":0}},{"id":5,"from_id":86784711,"owner_id":-132094733,"date":1478033649,"marked_as_ads":0,"post_type":"suggest","text":"Предложение 2","can_publish":1,"can_delete":1,"attachments":[{"type":"photo","photo":{"id":439659789,"album_id":-8,"owner_id":-132094733,"user_id":86784711,"photo_75":"https:\/\/pp.vk.me\/c626216\/v626216711\/36382\/3Az0m26K-A8.jpg","photo_130":"https:\/\/pp.vk.me\/c626216\/v626216711\/36383\/SR1f1He6goY.jpg","photo_604":"https:\/\/pp.vk.me\/c626216\/v626216711\/36384\/nMRqjGCXi7k.jpg","photo_807":"https:\/\/pp.vk.me\/c626216\/v626216711\/36385\/SKn6dd8rm3c.jpg","photo_1280":"https:\/\/pp.vk.me\/c626216\/v626216711\/36386\/jc3xPMSrcSg.jpg","width":1280,"height":768,"text":"","date":1478033649,"access_key":"5b3152b97c6ead9a89"}},{"type":"photo","photo":{"id":439659790,"album_id":-8,"owner_id":-132094733,"user_id":86784711,"photo_75":"https:\/\/pp.vk.me\/c626216\/v626216711\/3638b\/6ztqRN9VMSA.jpg","photo_130":"https:\/\/pp.vk.me\/c626216\/v626216711\/3638c\/TuxFgMbs7fE.jpg","photo_604":"https:\/\/pp.vk.me\/c626216\/v626216711\/3638d\/pTwwDg4NOlQ.jpg","photo_807":"https:\/\/pp.vk.me\/c626216\/v626216711\/3638e\/cuShzd52HOs.jpg","photo_1280":"https:\/\/pp.vk.me\/c626216\/v626216711\/3638f\/v_Lj1IwX9ic.jpg","width":1280,"height":768,"text":"","date":1478033649,"access_key":"6fa03a22a376d8fe18"}}],"post_source":{"type":"api","platform":"android"},"comments":{"count":0,"can_post":0}},{"id":4,"from_id":86784711,"owner_id":-132094733,"date":1478033591,"marked_as_ads":0,"post_type":"suggest","text":"Предложение 1","can_publish":1,"can_delete":1,"attachments":[{"type":"photo","photo":{"id":439659638,"album_id":-8,"owner_id":-132094733,"user_id":86784711,"photo_75":"https:\/\/pp.vk.me\/c626216\/v626216711\/3636e\/qB8Du4dJ-jw.jpg","photo_130":"https:\/\/pp.vk.me\/c626216\/v626216711\/3636f\/WTpLu47IVRM.jpg","photo_604":"https:\/\/pp.vk.me\/c626216\/v626216711\/36370\/TgbrGAmkbbw.jpg","photo_807":"https:\/\/pp.vk.me\/c626216\/v626216711\/36371\/ZSBaaJTwvlg.jpg","photo_1280":"https:\/\/pp.vk.me\/c626216\/v626216711\/36372\/s15xAOSQe1o.jpg","photo_2560":"https:\/\/pp.vk.me\/c626216\/v626216711\/36373\/A6MHcoc4sL0.jpg","width":768,"height":1280,"text":"","date":1478033591,"access_key":"f1758a76c72429eae7"}},{"type":"photo","photo":{"id":439659640,"album_id":-8,"owner_id":-132094733,"user_id":86784711,"photo_75":"https:\/\/pp.vk.me\/c626216\/v626216711\/36378\/piZLsLxRHFo.jpg","photo_130":"https:\/\/pp.vk.me\/c626216\/v626216711\/36379\/AWLGYNGfkHQ.jpg","photo_604":"https:\/\/pp.vk.me\/c626216\/v626216711\/3637a\/j67ZIboTItU.jpg","photo_807":"https:\/\/pp.vk.me\/c626216\/v626216711\/3637b\/toXoVUMbXFA.jpg","photo_1280":"https:\/\/pp.vk.me\/c626216\/v626216711\/3637c\/UgTBcndQm_Y.jpg","
        11-02 00:46:46.559 871-871/? E/VkGetOffers: PARSED NUL
*/

    private static String LOG_TAG = "VkPost";

    public static String SIZE_130 = "photo_130";
    public static String SIZE_604 = "photo_604";
    public static String SIZE_807 = "photo_807";
    public static String SIZE_1280 = "photo_1280";


    JSONObject mPost;

    public VkPost(JSONObject post) {
        mPost = post;
    }

    public int getId() {
        try {
            return mPost.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getText() {
        try {
            return mPost.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<String> getPhotoArray(String size) {
        try {
            JSONArray attachments = mPost.getJSONArray("attachments");
            Log.e(LOG_TAG, attachments.toString());
            ArrayList<String> res = new ArrayList<>();
            for (int i = 0; i < attachments.length(); i++) {
                if (attachments.getJSONObject(i).getString("type").equals("photo")) {
                    res.add(attachments.getJSONObject(i).getJSONObject("photo").getString(size));
                }
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public VKAttachments getAttachments() {
        VKAttachments attachments = new VKAttachments();
        try {
            VKApiPost vkApiPost = new VKApiPost(mPost);
            attachments = vkApiPost.attachments;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attachments;
    }

}
