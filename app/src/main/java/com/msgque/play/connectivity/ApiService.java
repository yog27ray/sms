package com.msgque.play.connectivity;

import com.msgque.play.model.AccessToken;
import com.msgque.play.model.CampaignModel;
import com.msgque.play.model.DomainModel;
import com.msgque.play.model.GroupModel;
import com.msgque.play.model.RequestBodyModel;
import com.msgque.play.model.RouteModel;
import com.msgque.play.model.SenderIdModel;
import com.msgque.play.model.SmsModel;
import com.msgque.play.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


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

  @POST("/api/users/signup")
  Call<ResponseBody> signUpUser(@Body UserModel user);

  @POST("/api/users/endUser")
  Call<UserModel> createEndCustomer(@Body UserModel body);

  @POST("/api/users/googleLogin")
  Call<AccessToken> googleLogin(@Body UserModel user);

  @GET("/api/users/me")
  Call<UserModel> getUserInfo();

  @PUT("/api/users/me")
  Call<ResponseBody> setUserInfo(@Body UserModel user);

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

  @GET("/api/campaigns")
  Call<ResponseBody> fetchCampaigns();

  @GET("/api/campaigns/{id_name}")
  Call<CampaignModel> fetchCampaign(@Path("id_name") String campaign);

  @GET("/api/routes")
  Call<List<RouteModel>> fetchRoutes();

  @GET("/api/senderId")
  Call<List<SenderIdModel>> fetchSenderIds(@Query("fl") String fl, @Query("status") String status);

  @POST("/api/sms")
  Call<ResponseBody> sendSms(@Body SmsModel body);

  @GET("/api/domains")
  Call<List<DomainModel>> getDomains();

  @POST("/api/domains")
  Call<ResponseBody> createDomains(@Body DomainModel domains);

  @POST("/api/domains/bulk")
  Call<ResponseBody> createDomains(@Body List<DomainModel> domains);

}
