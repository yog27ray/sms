package com.yog27ray.contactsync.dagger.component;

import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.dagger.module.StorageModule;
import com.yog27ray.contactsync.dagger.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(
		modules = {
				StorageModule.class,
		}, dependencies = { AppComponent.class }
)
public interface StorageComponent {

	SPUtility getSPUtility();
}
