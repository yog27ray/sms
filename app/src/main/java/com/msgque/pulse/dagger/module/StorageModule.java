package com.msgque.pulse.dagger.module;


import com.msgque.pulse.App;
import com.msgque.pulse.common.JsonConverter;
import com.msgque.pulse.common.SPHelper;
import com.msgque.pulse.model.UserModel;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

	@Provides
  SPHelper provideSPHelper (App application, JsonConverter jsonConverter) {
		return new SPHelper(application, jsonConverter);
	}

	@Provides
	UserModel provideUserDetail (SPHelper SPHelper) {
		return SPHelper.getCurrentUser();
	}

}
