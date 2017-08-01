package com.msgque.pulse.connectivity.interceptor;

import android.util.Base64;

import com.msgque.pulse.common.constant.EndPoints;
import com.msgque.pulse.common.constant.ServerConstant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
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
    Timber.d(original.url().toString());
    return chain.proceed(request);
  }
}
