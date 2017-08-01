package com.msgque.pulse.dagger.component;

import com.msgque.pulse.activity.SplashActivity;
import com.msgque.pulse.common.SPHelper;
import com.msgque.pulse.dagger.module.StorageModule;
import com.msgque.pulse.dagger.scope.ApplicationScope;

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
