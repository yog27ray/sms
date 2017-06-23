package com.yog27ray.contactsync;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.yog27ray.contactsync.common.JsonConverter;
import com.yog27ray.contactsync.common.SPUtility;
import com.yog27ray.contactsync.common.constant.EndPoints;
import com.yog27ray.contactsync.dagger.component.AppComponent;
import com.yog27ray.contactsync.dagger.component.DaggerAppComponent;
import com.yog27ray.contactsync.dagger.component.DaggerInternetComponent;
import com.yog27ray.contactsync.dagger.component.DaggerStorageComponent;
import com.yog27ray.contactsync.dagger.component.InternetComponent;
import com.yog27ray.contactsync.dagger.component.StorageComponent;
import com.yog27ray.contactsync.dagger.module.AppModule;
import com.yog27ray.contactsync.model.UserModel;

import timber.log.Timber;

public class App extends Application {

	private static Context appContext;
	private static InternetComponent internetComponent;
	private static StorageComponent storageComponent;

	public static InternetComponent getInternetComponent() {
		return internetComponent;
	}

	public static StorageComponent getStorageComponent() {
		return storageComponent;
	}

	public static void logout() {
//		new SPUtility(appContext, new JsonConverter()).reset();
//		Intent intent = new Intent(appContext, LoginActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
//				| Intent.FLAG_ACTIVITY_NEW_TASK);
//		appContext.startActivity(intent);
	}

	@Override
	public void onCreate () {
		super.onCreate();

		appContext = getApplicationContext();
		EndPoints.initialize(appContext);

		//setup Dagger
		setupDagger();

		//Initialize Timber
//		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
//		} else {
//			Fabric.with(this, new Crashlytics());
//			Timber.plant(new CrashlyticsTree());
//		}

		// TODO: Move this to where you establish a user session
		logUser();

		Log.e("DEBUG", String.valueOf(BuildConfig.DEBUG));
	}

	private void setupDagger () {

		AppModule appModule = new AppModule(this);

		AppComponent appComponent = DaggerAppComponent.builder()
				.appModule(appModule)
				.build();

		appComponent.inject(this);

		storageComponent = DaggerStorageComponent.builder()
				.appComponent(appComponent)
				.build();

		internetComponent = DaggerInternetComponent.builder()
				.appComponent(appComponent)
				.build();
	}

	private void logUser () {
		SPUtility spUtility = new SPUtility(getApplicationContext(), new JsonConverter());
		UserModel user = spUtility.getCurrentUser();
		// TODO: Use the current user's information
		// You can call any combination of these three methods
		if (!BuildConfig.DEBUG && user != null) {
			Crashlytics.setUserIdentifier(String.valueOf(user.id));
			Crashlytics.setUserEmail(user.email_id);
			Crashlytics.setUserName(user.name);
		}
	}

	private class CrashlyticsTree extends Timber.Tree {
		private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
		private static final String CRASHLYTICS_KEY_TAG = "tag";
		private static final String CRASHLYTICS_KEY_MESSAGE = "message";

		@Override
		protected void log (int priority, @Nullable String tag,
                        @Nullable String message, @Nullable Throwable t) {
			if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
				return;
			}

			Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
			Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
			Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

			if (t == null) {
				Crashlytics.logException(new Exception(message));
			} else {
				Crashlytics.logException(t);
			}
		}
	}
}
