package com.msgque.pulse.dagger.module;

import com.msgque.pulse.App;
import com.msgque.pulse.common.JsonConverter;
import com.msgque.pulse.common.SPHelper;
import com.msgque.pulse.common.constant.EndPoints;
import com.msgque.pulse.connectivity.RetroFitConnector;
import com.msgque.pulse.connectivity.ServerConnection;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class InternetModule {

  private final String apiURL;
  private final String dlrMsgqueUrl;

  public InternetModule() {
    this.apiURL = EndPoints.API_SERVER;
    this.dlrMsgqueUrl = EndPoints.DLR_MSGQUE_SERVER;
  }

  @Provides
  Cache provideOkHttpCache(App application) {
    int cacheSize = 10 * 1024 * 1024; // 10 MiB
    return new Cache(application.getCacheDir(), cacheSize);
  }

  @Provides
  List<Retrofit.Builder> provideRetrofitBuilder(JsonConverter gson) {
    List<Retrofit.Builder> list = new ArrayList<>();
    list.add(new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson.getGson()))
        .baseUrl(apiURL));
    list.add(new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson.getGson()))
        .baseUrl(dlrMsgqueUrl));
    return list;
  }

  @Provides
  List<RetroFitConnector> provideRetrofitConnector(Cache cache, List<Retrofit.Builder> builders,
                                                   SPHelper spHelper) {
    List<RetroFitConnector> list = new ArrayList<>();
    for (Retrofit.Builder builder : builders) {
      list.add(new RetroFitConnector(cache, builder, spHelper));
    }
    return list;
  }


  @Provides
  ServerConnection provideQuezxConnection(List<RetroFitConnector> connectors, JsonConverter gson,
                                          SPHelper SPHelper) {
    return new ServerConnection(connectors, gson, SPHelper);
  }

}
