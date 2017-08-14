package com.msgque.play.dagger.module;


import com.msgque.play.App;
import com.msgque.play.common.JsonConverter;
import com.msgque.play.common.SPHelper;
import com.msgque.play.model.UserModel;

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
