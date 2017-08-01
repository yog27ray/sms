package com.msgque.pulse.helper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.msgque.pulse.model.UserModel;

import timber.log.Timber;

public class LoginHelper {
  private static final int RC_SIGN_IN = 1;

  private GoogleApiClient googleApiClient;
  private AppCompatActivity activity;
  private LoginListener loginListener;
  private LogoutListener logoutListener;
  private GoogleApiClient.OnConnectionFailedListener googleApiListener =
      new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
          Timber.e("Connection Failed");
        }
      };

  public void initGoogleLogin(AppCompatActivity activity) {
    this.activity = activity;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();
    googleApiClient = new GoogleApiClient.Builder(activity)
        .enableAutoManage(activity, googleApiListener)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
  }

  public void googleSignIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    activity.startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  private void handleSignInResult(GoogleSignInResult result) {
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount acct = result.getSignInAccount();
      UserModel user = new UserModel();
      user.id = acct.hashCode();
      user.email = acct.getEmail();
      user.name = acct.getDisplayName();
      if (loginListener != null) loginListener.success(user);
      return;
    }
    if (loginListener != null) loginListener.failure();
  }

  public void setLoginListener(LoginListener loginListener) {
    this.loginListener = loginListener;
  }

  public void setLogoutListener(LogoutListener logoutListener) {
    this.logoutListener = logoutListener;
  }

  public void logout() {
    googleLogout();
  }

  private void googleLogout() {
    // Google sign out
    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
        new ResultCallback<Status>() {
          @Override
          public void onResult(@NonNull Status status) {
            if (logoutListener != null) logoutListener.logout(true);
          }
        });
  }

  public interface LoginListener {
    void success(UserModel user);

    void failure();
  }

  public interface LogoutListener {
    void logout(boolean status);
  }
}
