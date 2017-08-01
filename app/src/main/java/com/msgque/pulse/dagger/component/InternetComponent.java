package com.msgque.pulse.dagger.component;


import com.msgque.pulse.activity.EditProfileActivity;
import com.msgque.pulse.activity.GroupListActivity;
import com.msgque.pulse.activity.LoginActivity;
import com.msgque.pulse.activity.SendSmsActivity;
import com.msgque.pulse.activity.domain.ConfirmDomainPurchaseActivity;
import com.msgque.pulse.activity.domain.SearchDomainActivity;
import com.msgque.pulse.connectivity.interceptor.AuthorizationInterceptor;
import com.msgque.pulse.dagger.module.InternetModule;
import com.msgque.pulse.dagger.module.StorageModule;
import com.msgque.pulse.dagger.scope.ApplicationScope;

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

  void inject(EditProfileActivity activity);

  void inject(SearchDomainActivity activity);

  void inject(ConfirmDomainPurchaseActivity activity);
}
