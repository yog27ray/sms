package com.yog27ray.contactsync.connectivity;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.connectivity.interceptor.AuthorizationInterceptor;
import com.yog27ray.contactsync.connectivity.interceptor.LoginInterceptor;

import java.io.IOException;

import retrofit.Retrofit;

public class RetroFitConnector {

	private final SPUtility spUtility;
	private final OkHttpClient httpClient;
	private final Retrofit.Builder builder;

	public RetroFitConnector (OkHttpClient httpClient, Retrofit.Builder builder, SPUtility spUtility) {
		this.httpClient = httpClient;
		this.builder = builder;
		this.spUtility = spUtility;
	}

	<S> S createService(Class<S> serviceClass, ServerConnection connection) {
		httpClient.interceptors().clear();
		httpClient.interceptors().add(new AuthorizationInterceptor(spUtility, httpClient, connection));

		Retrofit retrofit = builder.client(httpClient).build();
		return retrofit.create(serviceClass);
	}

	public <S> S createServiceNoAuthentication (Class<S> serviceClass) {
		httpClient.interceptors().clear();
		httpClient.interceptors().add(new Interceptor() {
			@Override
			public Response intercept (Chain chain) throws IOException {
				Request original = chain.request();

				Request.Builder requestBuilder = original.newBuilder()
						.method(original.method(), original.body());

				Request request = requestBuilder.build();
//        Timber.d(request.method() + ":" + request.urlString());
        return chain.proceed(request);
			}
		});

		Retrofit retrofit = builder.client(httpClient).build();
		return retrofit.create(serviceClass);
	}

	<S> S createLoginService(Class<S> serviceClass) {
		httpClient.interceptors().clear();
		httpClient.interceptors().add(new LoginInterceptor());

		Retrofit retrofit = builder.client(httpClient).build();
		return retrofit.create(serviceClass);
	}
}
