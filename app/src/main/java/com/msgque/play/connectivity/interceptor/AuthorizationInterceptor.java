package com.msgque.play.connectivity.interceptor;

import com.msgque.play.App;
import com.msgque.play.BuildConfig;
import com.msgque.play.common.SPHelper;
import com.msgque.play.common.constant.ServerConstant;
import com.msgque.play.connectivity.ServerConnection;
import com.msgque.play.model.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AuthorizationInterceptor implements Interceptor {

  private final SPHelper spHelper;
  private final ServerConnection connection;

  public AuthorizationInterceptor(SPHelper spHelper, ServerConnection connection) {
    this.spHelper = spHelper;
    this.connection = connection;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    AccessToken token = spHelper.getAccessToken();
    //Build new request
    Request.Builder builder = request.newBuilder();
    request = builder.build();
    String basic = "no-token-found";
    if (token != null) {
      basic = String.format("Bearer %s", token.access_token);
      builder = builder.header(ServerConstant.AUTHORIZATION, basic);
      if (spHelper.isAdmin()) builder = builder.header(ServerConstant.ADMIN, "true");
      builder = builder.method(request.method(), request.body());
      request = builder.build();
    }
    if (BuildConfig.DEBUG) {
      Timber.d(basic);
      Timber.d(request.method() + " : " + request.url().toString());
    }
    Response response = chain.proceed(request);
    if (response.code() != 200)
      Timber.d(request.method() + " : " + request.url().toString() + ":" + response.code());
    if (response.code() == 401) { //if unauthorized
      String requestToken = request.headers().get(ServerConstant.AUTHORIZATION).split(" ")[1];
      synchronized (connection) { //perform all 401 in sync blocks, to avoid multiply token updates
        AccessToken accessToken = spHelper.getAccessToken(); //get currently stored token
        if(accessToken != null && accessToken.access_token.equals(requestToken)) { //isDuplicate current token with token that was stored before, if it was not updated - do update
          int code = connection.refreshAccessToken(accessToken);
          if (code != 200) {
            App.logout();
            return response;
          }
          accessToken = spHelper.getAccessToken();
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
