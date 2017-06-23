package com.yog27ray.contactsync.connectivity.interceptor;

import com.google.common.base.Joiner;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.BuildConfig;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.common.constant.ServerConstant;
import com.yog27ray.contactsync.connectivity.ServerConnection;
import com.yog27ray.contactsync.model.AccessToken;

import java.io.IOException;

import timber.log.Timber;

public class AuthorizationInterceptor implements Interceptor {

  private final SPUtility spUtility;
  private final OkHttpClient httpClient;
  private final ServerConnection connection;

  public AuthorizationInterceptor(SPUtility spUtility, OkHttpClient httpClient,
                                  ServerConnection connection) {
    this.spUtility = spUtility;
    this.httpClient = httpClient;
    this.connection = connection;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    AccessToken token = spUtility.getAccessToken();
    //Build new request
    Request.Builder builder = request.newBuilder();
    request = builder.build();
    String basic = "no-token-found";
    if (token != null) {
      basic = String.format("Bearer %s", token.access_token);
      builder = builder.header(ServerConstant.AUTHORIZATION, basic);
      if (spUtility.isAdmin()) {
        builder = builder.header(ServerConstant.ADMIN, "true");
        if (spUtility.getAdminViewUsers() != null) {
          String userIds = Joiner.on(",").join(spUtility.getAdminViewUsers());
          builder = builder.header(ServerConstant.USER_IDS, userIds);
        }
      }
      builder = builder.method(request.method(), request.body());
      request = builder.build();
    }
    if (BuildConfig.DEBUG) {
      Timber.d(basic);
      Timber.d(request.method() + " : " + request.urlString());
    }
    Response response = chain.proceed(request);
    if (response.code() != 200)
      Timber.d(request.method() + " : " + request.urlString() + ":" + response.code());
    if (response.code() == 401) { //if unauthorized
      String requestToken = request.headers().get(ServerConstant.AUTHORIZATION).split(" ")[1];
      synchronized (httpClient) { //perform all 401 in sync blocks, to avoid multiply token updates
        AccessToken accessToken = spUtility.getAccessToken(); //get currently stored token
        if(accessToken != null && accessToken.access_token.equals(requestToken)) { //isDuplicate current token with token that was stored before, if it was not updated - do update
          int code = connection.refreshAccessToken(accessToken);
          if (code != 200) {
            App.logout();
            return response;
          }
          accessToken = spUtility.getAccessToken();
        }
        if (accessToken == null) return response;
        //retry with new auth token,
        builder.header(
            ServerConstant.AUTHORIZATION, String.format("Bearer %s", accessToken.access_token))
            .method(request.method(), request.body());

        Request newRequest = builder.build();
        return chain.proceed(newRequest); //repeat request with new token
      }
    }

    return response;
  }
}
