package com.msgque.pulse.dagger.component;


import com.msgque.pulse.App;
import com.msgque.pulse.common.JsonConverter;
import com.msgque.pulse.dagger.module.AppModule;
import com.msgque.pulse.helper.AppHelper;
import com.msgque.pulse.helper.ContactHelper;
import com.msgque.pulse.helper.ListHelper;
import com.msgque.pulse.helper.LoginHelper;
import com.msgque.pulse.helper.PermissionHelper;
import com.msgque.pulse.helper.UIHelper;

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

