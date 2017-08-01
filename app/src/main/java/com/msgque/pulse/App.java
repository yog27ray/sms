package com.msgque.pulse;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.msgque.pulse.activity.LoginActivity;
import com.msgque.pulse.common.JsonConverter;
import com.msgque.pulse.common.SPHelper;
import com.msgque.pulse.common.constant.EndPoints;
import com.msgque.pulse.dagger.component.AppComponent;
import com.msgque.pulse.dagger.component.DaggerAppComponent;
import com.msgque.pulse.dagger.component.DaggerInternetComponent;
import com.msgque.pulse.dagger.component.DaggerStorageComponent;
import com.msgque.pulse.dagger.component.InternetComponent;
import com.msgque.pulse.dagger.component.StorageComponent;
import com.msgque.pulse.dagger.module.AppModule;
import com.msgque.pulse.model.UserModel;

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
		new SPHelper(appContext, new JsonConverter()).reset();
		Intent intent = new Intent(appContext, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		appContext.startActivity(intent);
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
		SPHelper spHelper = new SPHelper(getApplicationContext(), new JsonConverter());
		UserModel user = spHelper.getCurrentUser();
		// TODO: Use the current user's information
		// You can call any combination of these three methods
		if (!BuildConfig.DEBUG && user != null) {
			Crashlytics.setUserIdentifier(String.valueOf(user.id));
			Crashlytics.setUserEmail(user.email);
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
