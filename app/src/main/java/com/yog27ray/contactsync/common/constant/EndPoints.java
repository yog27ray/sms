package com.yog27ray.contactsync.common.constant;

import android.content.Context;

import com.yog27ray.contactsync.R;

public class EndPoints {

	public static String API_SERVER;
	public static String CLIENT_APP_ID;
	public static String CLIENT_SECRET;

	public static void initialize(Context context) {
		API_SERVER = context.getString(R.string.URL_API);
		CLIENT_APP_ID = context.getString(R.string.CLIENT_APP_ID);
		CLIENT_SECRET = context.getString(R.string.CLIENT_SECRET);
	}
}
