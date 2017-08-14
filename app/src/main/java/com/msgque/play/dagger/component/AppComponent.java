package com.msgque.play.dagger.component;


import com.msgque.play.App;
import com.msgque.play.common.JsonConverter;
import com.msgque.play.dagger.module.AppModule;
import com.msgque.play.helper.AppHelper;
import com.msgque.play.helper.ContactHelper;
import com.msgque.play.helper.ListHelper;
import com.msgque.play.helper.LoginHelper;
import com.msgque.play.helper.PermissionHelper;
import com.msgque.play.helper.UIHelper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {
        AppModule.class
    }
)
public interface AppComponent {

  App getApp();

  JsonConverter getJsonConverter();

  ContactHelper getContactHelper();

  PermissionHelper getPermissionHelper();

  ListHelper getListHelper();

  AppHelper getAppHelper();

  LoginHelper getLoginHelper();

  UIHelper getUIHelper();

  void inject(App app);
}

