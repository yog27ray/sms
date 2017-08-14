package com.msgque.play.dagger.component;

import com.msgque.play.activity.SplashActivity;
import com.msgque.play.common.SPHelper;
import com.msgque.play.dagger.module.StorageModule;
import com.msgque.play.dagger.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(
		modules = {
				StorageModule.class,
		}, dependencies = { AppComponent.class }
)
public interface StorageComponent {

	SPHelper getSPHelper();

  void inject(SplashActivity activity);
}
