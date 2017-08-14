package com.msgque.play.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.msgque.play.common.constant.SharedConstant;
import com.msgque.play.model.AccessToken;
import com.msgque.play.model.UserModel;

import java.util.Calendar;
import java.util.List;

public class SPHelper {
  private final JsonConverter jsonConverter;
  private final SharedPreferences sharedPreferences;

  public SPHelper(Context context, JsonConverter jsonConverter) {
    sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    this.jsonConverter = jsonConverter;
  }

  public AccessToken getAccessToken() {
    AccessToken token = null;
    if (!sharedPreferences.getString(SharedConstant.TOKEN_DATA, "").equals("")) {
      token = jsonConverter.fromJson(
          sharedPreferences.getString(SharedConstant.TOKEN_DATA, ""), AccessToken.class);
    }
    return token;
  }

  public void setAccessToken(AccessToken accessToken) {
    accessToken.start_time = Calendar.getInstance().getTimeInMillis();
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(SharedConstant.TOKEN_DATA, jsonConverter.toJson(accessToken));
    editor.apply();
  }

  public void reset() {
    sharedPreferences.edit().clear().apply();
  }

  public boolean isUserLoggedIn() {
    return getCurrentUser() != null;
  }

  public UserModel getCurrentUser() {
    UserModel user = null;
    if (!sharedPreferences.getString(SharedConstant.CURRENT_USER, "").equals("")) {
      user = jsonConverter.fromJson(
          sharedPreferences.getString(SharedConstant.CURRENT_USER, ""), UserModel.class);
    }
    return user;
  }

  public void setCurrentUser(UserModel user) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(SharedConstant.CURRENT_USER, jsonConverter.toJson(user));
    editor.apply();
  }

  public void setCacheState(boolean state) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(SharedConstant.CACHE_UPDATED, state);
    editor.apply();
  }

  public boolean isCacheUpdated() {
    return sharedPreferences.getBoolean(SharedConstant.CACHE_UPDATED, false);
  }

  public void setAdmin(boolean status) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(SharedConstant.ADMIN_FLAG, status);
    editor.apply();
  }

  public boolean isAdmin() {
    return sharedPreferences.getBoolean(SharedConstant.ADMIN_FLAG, false);
  }

  public void setAdminViewUsers(List<Integer> selectedUsers) {
    if (selectedUsers == null) return;
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(SharedConstant.ADMIN_AS_USER, jsonConverter.toJson(selectedUsers));
    editor.apply();
  }

  public List<Integer> getAdminViewUsers() {
    List<Integer> selectedUsers = null;
    if (!sharedPreferences.getString(SharedConstant.ADMIN_AS_USER, "").equals("")) {
      selectedUsers = jsonConverter.fromJson(
          sharedPreferences.getString(SharedConstant.ADMIN_AS_USER, ""),
          new TypeToken<List<Integer>>() {
          }.getType());
    }
    return selectedUsers;
  }

  public boolean isProfileCompleted() {
    return sharedPreferences.getBoolean(SharedConstant.PROFILE_UPDATED, false);
  }

  public void setProfileCompleted(boolean state) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(SharedConstant.PROFILE_UPDATED, state);
    editor.apply();
  }
}
