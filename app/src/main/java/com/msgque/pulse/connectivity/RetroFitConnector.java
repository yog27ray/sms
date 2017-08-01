package com.msgque.pulse.connectivity;

import com.msgque.pulse.common.SPHelper;
import com.msgque.pulse.connectivity.interceptor.AuthorizationInterceptor;
import com.msgque.pulse.connectivity.interceptor.LoginInterceptor;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class RetroFitConnector {

	private final SPHelper spHelper;
	private final Cache cache;
	private final Retrofit.Builder builder;

	public RetroFitConnector(Cache cache, Retrofit.Builder builder, SPHelper spHelper) {
		this.cache = cache;
		this.builder = builder;
		this.spHelper = spHelper;
	}

	<S> S createService(Class<S> serviceClass, ServerConnection connection) {
		OkHttpClient httpClient = getOkHttpBuilder()
				.addInterceptor(new AuthorizationInterceptor(spHelper, connection))
				.build();

		Retrofit retrofit = builder.client(httpClient).build();
		return retrofit.create(serviceClass);
	}

	public <S> S createServiceNoAuthentication(Class<S> serviceClass) {
		OkHttpClient httpClient = getOkHttpBuilder()
				.addInterceptor(new Interceptor() {
					@Override
					public Response intercept(Chain chain) throws IOException {
						Request original = chain.request();

						Request.Builder requestBuilder = original.newBuilder()
								.method(original.method(), original.body());

						Request request = requestBuilder.build();
						Timber.d(original.url().toString());
						return chain.proceed(request);
					}
				}).build();
		Retrofit retrofit = this.builder.client(httpClient).build();
		return retrofit.create(serviceClass);
	}

	private OkHttpClient.Builder getOkHttpBuilder() {
		return new OkHttpClient.Builder()
				.cache(cache);
	}

	<S> S createLoginService(Class<S> serviceClass) {
		OkHttpClient httpClient = getOkHttpBuilder()
				.addInterceptor(new LoginInterceptor())
				.build();

		Retrofit retrofit = builder.client(httpClient).build();
		return retrofit.create(serviceClass);
	}
}
