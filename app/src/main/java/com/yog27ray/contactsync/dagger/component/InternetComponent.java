package com.yog27ray.contactsync.dagger.component;


import com.yog27ray.contactsync.activity.GroupListActivity;
import com.yog27ray.contactsync.activity.LoginActivity;
import com.yog27ray.contactsync.activity.SendSmsActivity;
import com.yog27ray.contactsync.connectivity.interceptor.AuthorizationInterceptor;
import com.yog27ray.contactsync.dagger.module.InternetModule;
import com.yog27ray.contactsync.dagger.module.StorageModule;
import com.yog27ray.contactsync.dagger.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(
		modules = {
				InternetModule.class, StorageModule.class,
		}, dependencies = { AppComponent.class }
)
public interface InternetComponent {

  void inject(AuthorizationInterceptor interceptor);

  void inject(LoginActivity activity);

  void inject(GroupListActivity activity);

  void inject(SendSmsActivity activity);
}
