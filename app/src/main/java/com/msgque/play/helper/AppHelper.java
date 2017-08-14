package com.msgque.play.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

public class AppHelper {

  public AppHelper() {
    Timber.tag(AppHelper.class.getSimpleName());
  }

  public Integer getAppVersion(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager()
          .getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (Exception e) {
      Timber.e("Error getting package info");
    }
    return 0;
  }

  public String getDeviceId(Context context) {
    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  public int getProcessId() {
    return android.os.Process.myPid();
  }

  @SuppressWarnings("deprecation")
  public Spanned fromHtml(String html) {
    Spanned result;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    } else {
      result = Html.fromHtml(html);
    }
    return result;
  }

  // to encode the password which is hash(userId)
  public String md5(String s) {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      StringBuilder hexString = new StringBuilder();
      for (byte aMessageDigest : messageDigest)
        hexString.append(Integer.toHexString(0xFF & aMessageDigest));
      return hexString.toString();

    } catch (final NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "NO_HASH_PASS";
  }

  public int getHashCode(String value) {
    return value.hashCode() % 20;
  }
}
