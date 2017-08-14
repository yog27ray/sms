package com.msgque.play.dagger.module;


import com.msgque.play.App;
import com.msgque.play.common.JsonConverter;
import com.msgque.play.helper.AppHelper;
import com.msgque.play.helper.ContactHelper;
import com.msgque.play.helper.ListHelper;
import com.msgque.play.helper.LoginHelper;
import com.msgque.play.helper.PermissionHelper;
import com.msgque.play.helper.UIHelper;

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