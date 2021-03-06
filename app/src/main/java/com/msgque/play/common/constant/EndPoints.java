package com.msgque.play.common.constant;

import android.content.Context;

import com.msgque.play.R;

public class EndPoints {

  public static String API_SERVER;
	public static String DLR_MSGQUE_SERVER;
	public static String CLIENT_APP_ID;
	public static String TOKEN;
	public static String CLIENT_SECRET;

	public static void initialize(Context context) {
		API_SERVER = context.getString(R.string.URL_API);
		DLR_MSGQUE_SERVER = context.getString(R.string.URL_DLR_MSGQUE);
		TOKEN = context.getString(R.string.token);
		CLIENT_APP_ID = context.getString(R.string.CLIENT_APP_ID);
		CLIENT_SECRET = context.getString(R.string.CLIENT_SECRET);
	}
}
