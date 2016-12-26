package com.technologies.mobile.free_exchange_admin.logic;

import com.vk.sdk.api.model.VKApiPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by diviator on 29.11.2016.
 */

public class VkPhotoConverter {

    public static JSONArray getPhotoObjectsFromVkPhotos(List<VKApiPhoto> vkApiPhotos) throws JSONException{
        JSONArray photos = new JSONArray();

        for( VKApiPhoto vkPhoto : vkApiPhotos ){
            JSONObject photo = new JSONObject();
            photo.put("photo_75",vkPhoto.photo_75);
            photo.put("photo_130",vkPhoto.photo_130);
            photo.put("photo_604",vkPhoto.photo_604);
            photo.put("photo_807",vkPhoto.photo_807);
            photo.put("photo_1280",vkPhoto.photo_1280);
            photo.put("photo_2560",vkPhoto.photo_2560);
            photos.put(photo);
        }

        return photos;
    }

}
