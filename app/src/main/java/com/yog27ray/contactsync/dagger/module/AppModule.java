package com.yog27ray.contactsync.dagger.module;


import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.helper.ContactHelper;
import com.yog27ray.contactsync.helper.ListHelper;
import com.yog27ray.contactsync.helper.PermissionHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides
  @Singleton
  public App provideApp() {
    return app;
  }

  @Provides
  @Singleton
  JsonConverter provideJsonConverter() {
    return new JsonConverter();
  }

  @Provides
  @Singleton
  ListHelper provideListHelper() {
    return new ListHelper();
  }

  @Provides
  @Singleton
  PermissionHelper providePermissionHelper() {
    return new PermissionHelper();
  }

  @Provides
  @Singleton
  ContactHelper provideContactHelper(ListHelper listHelper) {
    return new ContactHelper(listHelper);
  }
}