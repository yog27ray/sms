package com.msgque.play.dagger.component;


import com.msgque.play.activity.CampaignListActivity;
import com.msgque.play.activity.EditProfileActivity;
import com.msgque.play.activity.GroupListActivity;
import com.msgque.play.activity.LoginActivity;
import com.msgque.play.activity.SendSmsActivity;
import com.msgque.play.activity.WebActivity;
import com.msgque.play.activity.domain.ConfirmDomainPurchaseActivity;
import com.msgque.play.activity.domain.ExistingDomainActivity;
import com.msgque.play.activity.domain.SearchDomainActivity;
import com.msgque.play.connectivity.interceptor.AuthorizationInterceptor;
import com.msgque.play.dagger.module.InternetModule;
import com.msgque.play.dagger.module.StorageModule;
import com.msgque.play.dagger.scope.ApplicationScope;

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

  void inject(CampaignListActivity activity);

  void inject(WebActivity activity);

  void inject(ExistingDomainActivity activity);
}
