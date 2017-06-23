package com.yog27ray.contactsync.dagger.module;


import com.yog27ray.contactsync.App;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.model.UserModel;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

	@Provides
	SPUtility provideSharedPreferencesUtility (App application, JsonConverter jsonConverter) {
		return new SPUtility(application, jsonConverter);
	}

	@Provides
	UserModel provideUserDetail (SPUtility SPUtility) {
		return SPUtility.getCurrentUser();
	}

}
