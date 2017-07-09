package com.yog27ray.contactsync.connectivity;

import com.squareup.okhttp.ResponseBody;
import com.yog27ray.contactsync.model.AccessToken;
import com.yog27ray.contactsync.model.GroupModel;
import com.yog27ray.contactsync.model.RequestBodyModel;
import com.yog27ray.contactsync.model.RouteModel;
import com.yog27ray.contactsync.model.SenderIdModel;
import com.yog27ray.contactsync.model.SmsModel;
import com.yog27ray.contactsync.model.UserModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

interface ApiService {

  @FormUrlEncoded
  @POST("/oauth/token")
  Call<AccessToken> loginUser(@Field("username") String first, @Field("password") String last,
                              @Field("grant_type") String grant_type,
                              @Field("client_id") String clientId,
                              @Field("client_secret") String clientSecret);

  @FormUrlEncoded
  @POST("/oauth/token")
  Call<AccessToken> refreshToken(@Field("grant_type") String grant_type,
                                 @Field("refresh_token") String refresh_token);

  @GET("/api/users/me")
  Call<UserModel> getUserInfo();

  @POST("/api/users/otpLogin")
  Call<ResponseBody> generateOtp(@Body RequestBodyModel body);

  @POST("/api/users/otpVerify")
  Call<ResponseBody> verifyOtp(@Body RequestBodyModel body);

  @GET("/api/clients/{client_id}/users")
  Call<List<UserModel>> getClientUser(@Path("client_id") int clientId);

  @POST("/password_reset")
  Call<ResponseBody> forgotPassword(@Body RequestBodyModel body);

  @POST("/api/logout")
  Call<ResponseBody> logout(@Body AccessToken accessToken);

  @POST("/api/contacts/sync")
  Call<ResponseBody> syncContact(@Body RequestBodyModel body);

  @GET("/api/groups")
  Call<List<GroupModel>> fetchGroups();

  @GET("/api/routes")
  Call<List<RouteModel>> fetchRoutes();

  @GET("/api/senderId")
  Call<List<SenderIdModel>> fetchSenderIds(@Query("fl") String fl, @Query("status") String status);

  @POST("/api/sms")
  Call<ResponseBody> sendSms(@Body SmsModel body);
}
