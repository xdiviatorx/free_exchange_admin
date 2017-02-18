package com.technologies.mobile.free_exchange_admin.rest;

import com.technologies.mobile.free_exchange_admin.rest.model.CheckResponse;
import com.technologies.mobile.free_exchange_admin.rest.model.GetOffersResponse;
import com.technologies.mobile.free_exchange_admin.rest.model.IpUrl;
import com.technologies.mobile.free_exchange_admin.rest.model.SimpleResponse;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by diviator on 17.11.2016.
 */

public interface RestClient {

    String apiKey = "07642cafbcfa448e20d9a9a6ac798355";

    String WAITING_STATUS = "waiting";
    String APPROVED_STATUS = "approved";
    String REJECTED_STATUS = "rejected";

    @FormUrlEncoded
    @POST("API/getOffersByStatus")
    Call<GetOffersResponse> getOffersByStatus(@Field("status") String status, @Field("offset") int offset,
                                              @Field("count") int count, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/rejectOfferVk")
    Call<SimpleResponse> rejectOfferVk(@Field("post_id") int postId, @Field("admin_id") int adminId,
                                       @Field("message") String message, @Field("token") String token,
                                       @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/rejectOffer")
    Call<SimpleResponse> rejectOffer(@Field("id") int id, @Field("admin_id") int adminId,
                                     @Field("message") String message, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/approveOffer")
    Call<SimpleResponse> approveOffer(@Field("id") int id, @Field("type") JSONArray publishTo, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/approveOfferVk")
    Call<SimpleResponse> approveOfferVk(@Field("post_id") int postId, @Field("type") JSONArray publishTo, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/checkOfferVk")
    Call<CheckResponse> checkOfferVk(@Field("id") int id, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/checkOffer")
    Call<CheckResponse> checkOffer(@Field("id") int id, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/editAndCheckOffer")
    Call<CheckResponse> editAndCheckOffer(@Field("post_id") int postId, @Field("text") String message,
                                          @Field("photos") JSONArray photos, @Field("APIKey") String apiKey);

    @FormUrlEncoded
    @POST("API/getIPUrl")
    Call<IpUrl> getIpUrl(@Field("APIKey") String apiKey);

}
