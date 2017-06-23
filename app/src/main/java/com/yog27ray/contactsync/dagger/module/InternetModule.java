package com.yog27ray.contactsync.dagger.module;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.common.constant.EndPoints;
import com.yog27ray.contactsync.connectivity.ServerConnection;
import com.yog27ray.contactsync.connectivity.RetroFitConnector;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@Module
public class InternetModule {

  private final String apiURL;

  public InternetModule() {
    this.apiURL = EndPoints.API_SERVER;
  }

  @Provides
  Cache provideOkHttpCache(App application) {
    int cacheSize = 10 * 1024 * 1024; // 10 MiB
    return new Cache(application.getCacheDir(), cacheSize);
  }

  @Provides
  OkHttpClient provideOkHttpClient(Cache cache) {
    OkHttpClient client = new OkHttpClient();
    client.setCache(cache);
    return client;
  }

  @Provides
  List<Retrofit.Builder> provideRetrofitBuilder(JsonConverter gson, OkHttpClient okHttpClient) {
    List<Retrofit.Builder> list = new ArrayList<>();
    list.add(new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson.getGson()))
        .baseUrl(apiURL)
        .client(okHttpClient));
    return list;
  }

  @Provides
  List<RetroFitConnector> provideRetrofitConnector(OkHttpClient okHttpClient,
                                                   List<Retrofit.Builder> builders,
                                                   SPUtility spUtility) {
    List<RetroFitConnector> list = new ArrayList<>();
    for (Retrofit.Builder builder : builders) {
      list.add(new RetroFitConnector(okHttpClient, builder, spUtility));
    }
    return list;
  }


  @Provides
  ServerConnection provideQuezxConnection(List<RetroFitConnector> connectors, JsonConverter gson,
                                          SPUtility SPUtility) {
    return new ServerConnection(connectors, gson, SPUtility);
  }

}
