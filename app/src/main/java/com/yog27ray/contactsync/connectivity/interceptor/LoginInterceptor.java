package com.yog27ray.contactsync.connectivity.interceptor;

import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yog27ray.contactsync.common.constant.EndPoints;
import com.yog27ray.contactsync.common.constant.ServerConstant;

import java.io.IOException;

import timber.log.Timber;

public class LoginInterceptor implements Interceptor {

  private final String basic;

  public LoginInterceptor() {
    this.basic = "Basic " + Base64.encodeToString(( EndPoints.CLIENT_APP_ID + ":" +
        EndPoints.CLIENT_SECRET ).getBytes(), Base64.NO_WRAP);
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();

    Request.Builder requestBuilder = original.newBuilder()
        .header(ServerConstant.AUTHORIZATION, basic).method(original.method(), original.body());

    Request request = requestBuilder.build();
    Timber.d(basic);
    Timber.d(original.urlString());
    return chain.proceed(request);
  }
}
