package com.msgque.pulse.dagger.module;


import com.msgque.pulse.App;
import com.msgque.pulse.common.JsonConverter;
import com.msgque.pulse.helper.AppHelper;
import com.msgque.pulse.helper.ContactHelper;
import com.msgque.pulse.helper.ListHelper;
import com.msgque.pulse.helper.LoginHelper;
import com.msgque.pulse.helper.PermissionHelper;
import com.msgque.pulse.helper.UIHelper;

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
  public AppHelper provideAppHelper() {
    return new AppHelper();
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

  @Provides
  @Singleton
  LoginHelper provideLoginHelper() {
    return new LoginHelper();
  }

  @Provides
  @Singleton
  UIHelper provideUIHelper() {
    return new UIHelper();
  }
}