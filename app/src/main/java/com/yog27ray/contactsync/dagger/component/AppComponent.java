package com.yog27ray.contactsync.dagger.component;


import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.dagger.module.AppModule;
import com.yog27ray.contactsync.helper.ContactHelper;
import com.yog27ray.contactsync.helper.ListHelper;
import com.yog27ray.contactsync.helper.PermissionHelper;

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

  void inject(App app);
}

